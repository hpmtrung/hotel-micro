package com.hba.apigateway.proxies;

import com.hba.apigateway.rentals.RentalDestinations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class RentalService {

  private final WebClient webClient;
  private final RentalDestinations rentalDestinations;

  public Flux<HServiceDTO> getServices() {
    return webClient
        .get()
        .uri(rentalDestinations.getServiceGet())
        .retrieve()
        .bodyToFlux(HServiceDTO.class);
  }
}