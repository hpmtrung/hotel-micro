package com.hba.apigateway.partners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.OPTIONS;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Slf4j
@Configuration
@EnableConfigurationProperties(PartnerDestinations.class)
public class PartnerConfiguration {

  @Bean
  public RouterFunction<ServerResponse> authenticateRouting(PartnerHandler partnerHandler) {
    return RouterFunctions.route(
        POST("/api/authenticate"),
        request -> {
          log.info("Routing to authenticate endpoint");
          return partnerHandler.authenticate(request);
        });
  }
}