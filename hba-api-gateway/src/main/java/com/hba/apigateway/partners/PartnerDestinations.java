package com.hba.apigateway.partners;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.destinations.partner-service")
public class PartnerDestinations {
  private String authenticate;
  private String departmentGet;
}