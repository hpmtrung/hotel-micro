package com.hba.apigateway.rentals;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.destinations.rental-service")
public class RentalDestinations {
  private String serviceGet;
}