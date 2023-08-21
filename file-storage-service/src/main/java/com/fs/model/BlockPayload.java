package com.fs.model;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor
@ToString
public class BlockPayload {

  String checksum;
  byte[] data;

}