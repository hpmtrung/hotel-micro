package com.hba.apigateway.proxies;

import com.hba.apigateway.User;
import com.hba.apigateway.partners.CredentialDTO;
import com.hba.apigateway.partners.PartnerDestinations;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PartnerService {

  private final WebClient webClient;
  private final PartnerDestinations destinations;

  public Mono<User> authenticate(Mono<CredentialDTO> credential) {
    return webClient
        .post()
        .uri(destinations.getAuthenticate())
        .body(credential, CredentialDTO.class)
        .retrieve()
        .bodyToMono(User.class);
  }

  public Flux<DepartmentDTO> getDepartments() {
    return webClient
        .get()
        .uri(destinations.getDepartmentGet())
        .retrieve()
        .bodyToFlux(DepartmentDTO.class);
  }
}