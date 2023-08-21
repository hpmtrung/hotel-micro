package com.hba.reservationservice.api;

import com.hba.common.messaging.Command;
import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TempReservationCreateCommand implements Command {
  private ReservationDTO reservation;
}