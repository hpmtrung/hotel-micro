package com.fs.model;

import com.fs.domain.File;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class PutObjectResponseDTO {
  int id;
  String key;
  int latestVersion;
  LocalDateTime createdAt;
  LocalDateTime lastModified;
  PutResult result;

  public enum PutResult {
    OVERRIDE,
    CREATE,
    NOT_CHANGE
  }

  public PutObjectResponseDTO(File object, String key, PutResult result) {
    this.id = object.getId();
    this.latestVersion = object.getLatestVersion();
    this.lastModified = object.getLastModified();
    this.createdAt = object.getCreatedAt();
    this.key = key;
    this.result = result;
  }

}