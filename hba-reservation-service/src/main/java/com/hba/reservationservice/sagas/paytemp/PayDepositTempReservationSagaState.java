package com.hba.reservationservice.sagas.paytemp;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PayDepositTempReservationSagaState {
  private int reservationId;
}