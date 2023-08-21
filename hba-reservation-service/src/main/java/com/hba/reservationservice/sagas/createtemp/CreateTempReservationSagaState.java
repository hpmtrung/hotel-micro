package com.hba.reservationservice.sagas.createtemp;

import com.hba.partner.api.CustomerDTO;
import com.hba.reservationservice.domain.Reservation;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateTempReservationSagaState {

  private Integer reservationId;
  private LocalDate checkinAt;
  private LocalDate checkoutAt;
  private LocalDateTime createdAt;
  private String customerId;
  private Integer depositPercent;
  private Map<Integer, Integer> suiteIdMapping;

  public Reservation toReservation() {
    Reservation reservation = new Reservation();
    reservation.setPersonalId(customerId);
    reservation.setCheckinAt(checkinAt);
    reservation.setCheckoutAt(checkoutAt);
    reservation.setPersonalId(customerId);
    reservation.setCreatedAt(createdAt);
    reservation.setDepositPercent(depositPercent);
    return reservation;
  }
}