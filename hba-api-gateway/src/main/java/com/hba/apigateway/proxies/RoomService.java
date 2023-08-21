package com.hba.apigateway.proxies;

import com.hba.apigateway.rooms.RoomDestinations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class RoomService {

  private final WebClient webClient;
  private final RoomDestinations destinations;

 public Flux<SuiteStyleDTO> getSuiteStyles() {
   return webClient.get()
       .uri(destinations.getSuiteStyleGet())
       .retrieve()
       .bodyToFlux(SuiteStyleDTO.class);
 }

  public Flux<SuiteTypeDTO> getSuiteTypes() {
    return webClient.get()
        .uri(destinations.getSuiteTypeGet())
        .retrieve()
        .bodyToFlux(SuiteTypeDTO.class);
  }

}