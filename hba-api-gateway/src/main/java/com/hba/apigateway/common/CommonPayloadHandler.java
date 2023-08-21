package com.hba.apigateway.common;

import com.hba.apigateway.proxies.PartnerService;
import com.hba.apigateway.proxies.RentalService;
import com.hba.apigateway.proxies.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommonPayloadHandler {

  private final PartnerService partnerService;
  private final RentalService rentalService;
  private final RoomService roomService;

  public Mono<ServerResponse> getPayload(ServerRequest request) {
    return Mono.zip(
            roomService.getSuiteStyles().collectList(),
            roomService.getSuiteTypes().collectList(),
            partnerService.getDepartments().collectList(),
            rentalService.getServices().collectList())
        .map(CommonPayload::create)
        .flatMap(
            p ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                    .body(BodyInserters.fromValue(p))
                    .onErrorResume(
                        e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
  }
}