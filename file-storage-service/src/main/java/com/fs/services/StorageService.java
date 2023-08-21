package com.fs.services;

import com.fs.domain.Client;
import com.fs.model.DeleteObjectsResponseDTO;
import com.fs.model.ObjectDTO;
import com.fs.model.PutObjectResponseDTO;

public interface StorageService {

  PutObjectResponseDTO putObject(
      Client client, String workspace, String key, String metadata, byte[] file);

  DeleteObjectsResponseDTO deleteObjects(Client client, String workspace, Iterable<String> keys);

  ObjectDTO getObject(Client client, int workspaceId, String key);
}