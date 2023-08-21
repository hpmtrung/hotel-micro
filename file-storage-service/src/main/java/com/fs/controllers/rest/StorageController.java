package com.fs.controllers.rest;

import com.fs.domain.Client;
import com.fs.model.*;
import com.fs.services.StorageService;
import com.fs.services.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;

@RestController
@RequestMapping("/api/storage")
@Slf4j
@RequiredArgsConstructor
public class StorageController {

  private final StorageService storageService;
  private final WorkspaceService workspaceService;

  @PostMapping("/workspace/create")
  WorkspaceDTO createWorkspace(
      @RequestBody WorkspaceCreate dto, @AuthenticationPrincipal Client client) {
    log.info("Creating a workspace {} by {}", dto, client.getUsername());
    return workspaceService.createWorkspace(dto, client);
  }

  @PutMapping("/object")
  PutObjectResponseDTO putObject(
      @RequestParam String workspace,
      @RequestParam String key,
      @RequestPart MultipartFile file,
      @AuthenticationPrincipal Client client) {
    try {
      return storageService.putObject(client, workspace, key, null, file.getBytes());
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/object")
  ResponseEntity<Object> deleteObjects(
      @RequestParam String workspace,
      @RequestParam List<String> keys,
      @AuthenticationPrincipal Client client) {
    log.info("Deleting objects {}", List.of(workspace, keys));
    DeleteObjectsResponseDTO response = storageService.deleteObjects(client, workspace, keys);
    if (!response.getNotFoundKeys().isEmpty()) {
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/object")
  ResponseEntity<byte[]> getObject(
      @RequestParam int workspaceId,
      @RequestParam String key,
      @AuthenticationPrincipal Client client) {
    log.info("Getting object {}", List.of(workspaceId, key));

    ObjectDTO dto = storageService.getObject(client, workspaceId, key);

    HttpHeaders headers = new HttpHeaders();
    headers.add(CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getName() + "\"");
    headers.add(ACCESS_CONTROL_REQUEST_METHOD, "GET");
    headers.add(ACCESS_CONTROL_EXPOSE_HEADERS, "content-range, content-length, accept-ranges");
    if (dto.getMetadata() != null) {
      headers.add("USER_METADATA", dto.getMetadata());
    }

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .headers(headers)
        .body(dto.getData());
  }
}