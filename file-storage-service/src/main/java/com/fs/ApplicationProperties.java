package com.fs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

  Ftp ftp = new Ftp();

  @Getter
  @Setter
  public static class Ftp {
    private String host;
    private int port;
    private String userName;
    private String password;
    private String aesKey;
  }
}