package com.fs.model;

import lombok.Value;

@Value
public class ObjectDTO {
  byte[] data;
  String relPath;
  String name;
  String metadata;
}