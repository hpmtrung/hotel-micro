package com.hba.apigateway.rentals;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RentalDestinations.class)
public class RentalConfiguration {

}