package com.hba.apigateway.rooms;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RoomDestinations.class)
public class RoomConfiguration {

}