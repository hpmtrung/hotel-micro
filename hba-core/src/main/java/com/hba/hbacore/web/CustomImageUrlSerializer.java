package com.hba.hbacore.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CustomImageUrlSerializer extends StdSerializer<String> {

  private static final long serialVersionUID = 1L;

  private static final String URL_TEMPLATE =
      "http://localhost:8055/api/storage/object?workspaceId=6&key=%s";

  protected CustomImageUrlSerializer() {
    super(String.class);
  }

  @Override
  public void serialize(
      String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    jsonGenerator.writeString(String.format(URL_TEMPLATE, s));
  }
}