package com.hba.reservationservice.web;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class TempReservationCreateRequest {
  private LocalDate checkinAt;
  private LocalDate checkoutAt;
  private Map<Integer, Integer> suiteIdMapping;
}