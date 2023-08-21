package com.hba.apigateway.partners;

import com.hba.apigateway.TokenProvider;
import com.hba.apigateway.proxies.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PartnerHandler {

  private final TokenProvider tokenProvider;
  private final PartnerService partnerService;

  public Mono<ServerResponse> authenticate(ServerRequest request) {
    Mono<CredentialDTO> credential = request.body(BodyExtractors.toMono(CredentialDTO.class));
    return partnerService
        .authenticate(credential)
        .flatMap(
            user ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                    .body(
                        BodyInserters.fromValue(Map.of("token", tokenProvider.createToken(user)))))
        .onErrorResume(e -> ServerResponse.badRequest().build());
  }
}