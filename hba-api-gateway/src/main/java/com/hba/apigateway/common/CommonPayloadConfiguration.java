package com.hba.apigateway.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class CommonPayloadConfiguration {

  @Bean
  public RouterFunction<ServerResponse> getCommonPayload(CommonPayloadHandler handler) {
    return RouterFunctions.route(GET("/api/common/**"), handler::getPayload);
  }
}