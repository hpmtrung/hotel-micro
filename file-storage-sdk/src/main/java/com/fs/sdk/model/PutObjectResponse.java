package com.fs.sdk.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PutObjectResponse {
  int id;
  String key;
  int latestVersion;
  LocalDateTime createdAt;
  LocalDateTime lastModified;
  String result;
}