package com.fs.model;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class ClientCredential {
  String id;
  String secret;
}