package com.hba.reservationservice.config;

import com.fs.sdk.StorageService;
import com.fs.sdk.auth.FSCredential;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FSProperties.class)
public class FSConfiguration {

  @Bean
  public StorageService storageService(FSProperties properties) {
    FSCredential credential =
        new FSCredential(
            properties.getId(), properties.getSecret(), properties.getHost(), properties.getPort());
    return new StorageService(credential);
  }
}