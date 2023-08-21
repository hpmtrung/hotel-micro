package com.fs.sdk.model;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@AllArgsConstructor
public class GetObjectRequest {
  int workspaceId;
  String key;
}