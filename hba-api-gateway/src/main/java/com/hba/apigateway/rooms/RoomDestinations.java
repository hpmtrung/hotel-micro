package com.hba.apigateway.rooms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.destinations.room-service")
public class RoomDestinations {
  private String suiteTypeGet;
  private String suiteStyleGet;
}