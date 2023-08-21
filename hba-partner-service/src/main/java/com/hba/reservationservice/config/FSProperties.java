package com.hba.reservationservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.fs")
public class FSProperties {
  private String id;
  private String secret;
  private String host;
  private int port;
}