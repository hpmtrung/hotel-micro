package com.hba.roomservice.messaging;

import com.hba.reservationservice.api.ReservationServiceChannels;
import com.hba.roomservice.domains.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@KafkaListener(
    id = "reservationDomainEventConsumer",
    topics = ReservationServiceChannels.DOMAIN_CHANNEL)
@Service
@RequiredArgsConstructor
public class ReservationServiceEventConsumer {

  private final ReservationService reservationService;

  // @KafkaHandler
  // public void handleTempReservationCreatedEvent(ReservationCreatedEvent event) {
  //   log.info("Handle reservation created event with info {}", event);
  //   reservationService.persistReservation(event.getReservation());
  // }
}