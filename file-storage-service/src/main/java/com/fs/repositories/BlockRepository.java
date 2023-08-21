package com.fs.repositories;

import com.fs.domain.Block;
import com.fs.model.BlockAndOrder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, String> {

  String CACHE_BY_CHECKSUM = "BlockByChecksum";

  @Cacheable(value = CACHE_BY_CHECKSUM, unless = "#result == null")
  Optional<Block> findByChecksum(String checksum);

  @Query(value = "select * from uv_unused_blocks", nativeQuery = true)
  List<Block> findUnusedBlocks();

  @Query(
      value =
          "select b.*, T.order_number"
              + " from block b join ("
              + " select *"
              + " from file_version_block"
              + " where file_version_id = (select id from file_version where file_id = ?1 and version_number = ?2)"
              + ") as T on b.id = T.block_id"
              + " order by T.order_number",
      nativeQuery = true)
  List<BlockAndOrder> findBlocksOfVersion(int fileId, int versionNumber);
}