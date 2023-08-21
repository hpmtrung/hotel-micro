package com.fs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class FileStorageServiceApplication {

  public static void main(String[] args) {

    SpringApplication.run(FileStorageServiceApplication.class, args);
  }
}