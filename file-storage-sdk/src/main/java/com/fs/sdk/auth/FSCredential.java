package com.fs.sdk.auth;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class FSCredential {
  String id;
  String secret;
  String host;
  int port;
}