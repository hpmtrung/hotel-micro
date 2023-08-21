package com.fs.services.impl;

import com.fs.config.FTPGateway;
import com.fs.domain.*;
import com.fs.model.*;
import com.fs.repositories.BlockRepository;
import com.fs.repositories.FileRepository;
import com.fs.services.BlockEncryptor;
import com.fs.services.StorageService;
import com.fs.services.WorkspaceService;
import com.fs.services.exceptions.DataManipulateException;
import com.fs.util.ByteUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

import static com.fs.model.PutObjectResponseDTO.PutResult.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

  private static final Comparator<BlockAndOrder> COMPARATOR_BLOCK_ORDERNUMBER_ASC =
      Comparator.comparing(BlockAndOrder::getOrderNumber);
  private static MessageDigest messageDigest;

  static {
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      log.error("Fail to initiate the message digest");
    }
  }

  private final FileRepository fileRepository;
  private final WorkspaceService workspaceService;
  private final BlockRepository blockRepository;
  private final FTPGateway ftpGateway;
  private final EntityManager entityManager;
  private final FtpRemoteFileTemplate fileTemplate;
  private final BlockEncryptor blockEncryptor;

  @Value("${application.max-block-size}")
  private int maxBlockSize;

  private static String checksum(final byte[] data) {
    byte[] bytes = messageDigest.digest(data);
    return new BigInteger(1, bytes).toString();
  }

  private static Map.Entry<String, String> getPathAndName(String key) {
    int last = key.lastIndexOf('/');
    String relPath = last != -1 ? key.substring(0, last) : "";
    String name = key.substring(last + 1);
    return Map.entry(relPath, name);
  }

  @Override
  public PutObjectResponseDTO putObject(
      Client client, String workspace, String key, String metadata, byte[] file) {
    PutObjectResponseDTO.PutResult putResult;

    final Workspace ws = workspaceService.getWorkspace(workspace, client);

    Map.Entry<String, String> pathAndName = getPathAndName(key);
    log.info("Parse path and name {}", pathAndName);

    String relPath = pathAndName.getKey();
    String name = pathAndName.getValue();
    String checkSum = checksum(file);

    File object = fileRepository.findByNameAndRelPathAndWorkspace(name, relPath, ws).orElse(null);

    if (object == null) {
      object = createNewObject(name, relPath, checkSum, ws);
      putResult = CREATE;
    } else {
      if (checkSum.equals(object.getChecksum())) {
        return new PutObjectResponseDTO(object, key, NOT_CHANGE);
      } else {
        object.setLastModified(LocalDateTime.now());
        object.setLatestVersion(object.getLatestVersion() + 1);
        object.setChecksum(checkSum);
        putResult = OVERRIDE;
      }
    }

    if (metadata != null) {
      object.setMetadata(metadata);
    }

    FileVersion fileVersion = new FileVersion();
    fileVersion.setVersionNumber(object.getLatestVersion());
    object.addFileVersion(fileVersion);

    ByteBuffer byteBuffer = ByteBuffer.wrap(file);
    int blockCount = (file.length + maxBlockSize - 1) / maxBlockSize;

    log.info("blockCount={} fileLen={}", blockCount, file.length);

    Set<BlockPayload> newBlockPayloads = new HashSet<>(); // new blocks to send
    List<Block> blocksInOrder = new ArrayList<>();
    HashMap<String, Block> cachedBlocks = new HashMap<>();

    int remainingLen = file.length;

    for (int i = 0; i < blockCount; i++) {
      byte[] chunk = new byte[Math.min(remainingLen, maxBlockSize)];
      remainingLen -= maxBlockSize;

      byteBuffer.get(chunk, 0, chunk.length);

      // Compress data
      try {
        chunk = ByteUtil.compress(chunk);
      } catch (IOException e) {
        throw new DataManipulateException("Fail to compress data.", e);
      }

      // Compute checksum
      String chunkChecksum = checksum(chunk);

      Block block = cachedBlocks.get(chunkChecksum);
      if (block == null) {
        block = blockRepository.findByChecksum(chunkChecksum).orElse(null);
        if (block == null) {
          block = new Block(chunkChecksum);
          byte[] encryptedChunk = blockEncryptor.encrypt(chunk);
          newBlockPayloads.add(new BlockPayload(chunkChecksum, encryptedChunk));
        }
        cachedBlocks.put(chunkChecksum, block);
      }

      blocksInOrder.add(block);
    }

    // Send to FTP server
    sendBlocks(newBlockPayloads);

    blockRepository.saveAll(blocksInOrder);
    fileRepository.save(object);
    for (int i = 0; i < blocksInOrder.size(); i++) {
      entityManager.persist(new FileVersionBlock(fileVersion, blocksInOrder.get(i), i + 1));
    }

    return new PutObjectResponseDTO(object, key, putResult);
  }

  @Override
  public DeleteObjectsResponseDTO deleteObjects(
      Client client, String workspace, Iterable<String> keys) {
    final Workspace ws = workspaceService.getWorkspace(workspace, client);
    List<String> notFoundKeys = new ArrayList<>();

    for (String key : keys) {
      Map.Entry<String, String> pathAndName = getPathAndName(key);
      String relPath = pathAndName.getKey();
      String name = pathAndName.getValue();

      log.info("File object with name '{}' and relative path '{}'", name, relPath);

      File object = fileRepository.findByNameAndRelPathAndWorkspace(name, relPath, ws).orElse(null);
      if (object == null) {
        notFoundKeys.add(key);
      } else {
        // Delete object
        fileRepository.delete(object);

        // Find unused blocks, then delete on both storage and database
        List<Block> unusedBlocks = blockRepository.findUnusedBlocks();
        log.info("Deleting blocks: {}", unusedBlocks);
        for (Block block : unusedBlocks) {
          ftpGateway.deleteBlock(block.getChecksum());
        }

        blockRepository.deleteAll(unusedBlocks);
      }
    }

    return new DeleteObjectsResponseDTO(notFoundKeys);
  }

  @Override
  @Transactional(readOnly = true)
  public ObjectDTO getObject(Client client, int workspaceId, String key) {
    final Workspace ws = workspaceService.getWorkspace(workspaceId);

    Map.Entry<String, String> pathAndName = getPathAndName(key);
    String relPath = pathAndName.getKey();
    String name = pathAndName.getValue();

    log.info("File object with name '{}' and relative path '{}'", name, relPath);
    File object =
        fileRepository
            .findByNameAndRelPathAndWorkspace(name, relPath, ws)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    List<BlockAndOrder> blocks =
        blockRepository.findBlocksOfVersion(object.getId(), object.getLatestVersion());

    List<byte[]> chunks =
        fileTemplate.invoke(
            operations -> {
              List<byte[]> chs = new ArrayList<>();
              for (BlockAndOrder block : blocks) {
                operations.get(
                    "files/file-storage/" + block.getChecksum(),
                    inputStream -> {
                      byte[] chunk = IOUtils.toByteArray(inputStream);
                      chunk = blockEncryptor.decrypt(chunk);
                      chunk = ByteUtil.decompress(chunk);
                      chs.add(chunk);
                    });
              }
              return chs;
            });

    ByteBuffer byteBuffer = ByteBuffer.allocate(chunks.stream().mapToInt(a -> a.length).sum());
    chunks.forEach(byteBuffer::put);
    byte[] content = byteBuffer.array();

    return new ObjectDTO(content, relPath, name, object.getMetadata());
  }

  private void sendBlocks(Iterable<BlockPayload> payloads) {
    for (BlockPayload payload : payloads) {
      Message<byte[]> message =
          new GenericMessage<>(
              payload.getData(), new MessageHeaders(Map.of("file_name", payload.getChecksum())));
      ftpGateway.sendBlocks(message);
    }
  }

  private File createNewObject(String name, String relPath, String checkSum, Workspace ws) {
    return new File(name, relPath, 0, checkSum, LocalDateTime.now(), LocalDateTime.now(), ws);
  }
}