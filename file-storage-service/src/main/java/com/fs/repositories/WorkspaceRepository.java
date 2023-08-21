package com.fs.repositories;

import com.fs.domain.Workspace;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends CrudRepository<Workspace, Integer> {

  String CACHE_BY_NAME_AND_OWNERID = "WorkspaceByNameAndOwnerId";

  boolean existsByNameAndOwnerId(String name, UUID ownerId);

  @Cacheable(value = CACHE_BY_NAME_AND_OWNERID, unless = "#result == null")
  Optional<Workspace> findByNameAndOwnerId(String name, UUID ownerId);
}