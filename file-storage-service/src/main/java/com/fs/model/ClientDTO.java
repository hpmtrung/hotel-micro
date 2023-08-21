package com.fs.model;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
@ToString
public class ClientDTO {

  UUID id;
  String username;
  String secretKey;

}