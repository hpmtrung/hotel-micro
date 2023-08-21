package com.hba.hbacore.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomImageUrlSerializerTest {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testSerializeCorrect() throws JsonProcessingException {
    Foo foo = new Foo();
    String result = objectMapper.writeValueAsString(foo);
    System.out.println(result);
    Assertions.assertTrue(result.contains("\"imageUrl\":\"http://localhost:8055/api/storage/object?workspaceId=6&key=program/2.jpg\""));
  }

  private static class Foo {

    @JsonSerialize(using = CustomImageUrlSerializer.class)
    String imageUrl = "program/2.jpg";

  }

}