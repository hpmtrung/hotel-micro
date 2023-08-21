package com.fs.sdk.model;

import lombok.Data;
import lombok.ToString;

import java.io.File;

@Data
@ToString
public class PutObjectRequest {
  String workspace;
  String key;
  @ToString.Exclude File file;
  ObjectMetadata metadata;

  public PutObjectRequest(String workspace, String key, File file) {
    this.workspace = workspace;
    this.key = key;
    this.file = file;
  }
}