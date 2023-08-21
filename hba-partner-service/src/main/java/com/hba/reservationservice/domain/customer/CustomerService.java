package com.hba.reservationservice.domain.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void publish() {
    kafkaTemplate.send("customer_create", "hello new");
  }
}