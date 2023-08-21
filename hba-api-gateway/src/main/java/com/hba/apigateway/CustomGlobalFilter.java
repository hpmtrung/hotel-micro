package com.hba.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {
  private final TokenProvider tokenProvider;

  public CustomGlobalFilter(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    List<String> vals = exchange.getRequest().getHeaders().get("Authorization");
    if (vals != null && !vals.isEmpty()) {
      String token = vals.get(0);
      if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
        token = token.substring(6);

        Authentication authentication = tokenProvider.getAuthentication(token);
        if (authentication != null) {
          exchange
              .mutate()
              .request(
                  exchange
                      .getRequest()
                      .mutate()
                      .header("Authorization", "Bearer " + tokenProvider.createUnsignedToken(token))
                      .build())
              .build();
        }
      }
    }
    // log.info("Tracing {}", exchange.getRequest().getPath().value());
    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}