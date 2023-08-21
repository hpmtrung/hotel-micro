package com.fs.sdk.model;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Value
@ToString
@AllArgsConstructor
public class DeleteObjectsRequest {
  String workspace;
  List<String> keys;
}