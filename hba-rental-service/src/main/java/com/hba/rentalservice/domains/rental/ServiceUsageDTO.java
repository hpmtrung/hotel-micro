package com.hba.rentalservice.domains.rental;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ServiceUsageDTO {
  private int id;
  private LocalDateTime createdAt;
  private int amount;
  private int servicePrice;
  private int total;
  private boolean paid;
  private String employeeId;
  private String service;
}