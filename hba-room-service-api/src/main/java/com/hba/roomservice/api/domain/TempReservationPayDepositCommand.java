package com.hba.roomservice.api.domain;

import com.hba.common.messaging.Command;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TempReservationPayDepositCommand implements Command {
  private int reservationId;
  private boolean accept;
}