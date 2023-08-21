package com.fs.repositories;

import com.fs.domain.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends CrudRepository<Client, UUID> {
  Optional<Client> findByIdAndSecretKey(UUID id, String secretKey);

  boolean existsByUsername(String userName);
}