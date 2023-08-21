package com.fs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;

public class JacksonUtil {
  public static final ObjectMapper OBJECT_MAPPER = initObjectMapper();

  private JacksonUtil() {}

  private static ObjectMapper initObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    // mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  public static <T> T deserializeJson(String value, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(value, clazz);
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "The given string value cannot be transformed to Json object. [value="
              + (value != null ? value : "null")
              + "]",
          e);
    }
  }

  public static String serializeJson(Object value) {
    try {
      return "\n" + OBJECT_MAPPER.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(
          "The given object value cannot be transformed to a String.", e);
    }
  }
}