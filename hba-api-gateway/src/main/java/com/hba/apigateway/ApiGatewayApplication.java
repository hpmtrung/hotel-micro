package com.hba.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication(
    exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
public class ApiGatewayApplication {

  @Bean
  public TokenProvider tokenProvider(@Value("${application.security.secret}") String secret) {
    return TokenProvider.signedTokenProvider(secret);
  }

  @Bean
  public WebClient webClient() {
    return WebClient.create();
  }

  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayApplication.class, args);
  }
}