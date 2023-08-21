package com.hba.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
// @Component
public class CustomFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    log.info(
        "Tracing {}",
        Map.of(
            "path",
            exchange.getRequest().getPath().value(),
            "headers",
            exchange.getRequest().getHeaders()));
    return chain.filter(exchange);
  }
}