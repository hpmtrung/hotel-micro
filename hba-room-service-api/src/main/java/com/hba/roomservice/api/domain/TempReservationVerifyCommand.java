package com.hba.roomservice.api.domain;

import com.hba.common.messaging.Command;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TempReservationVerifyCommand implements Command {
  private LocalDate checkinAt;
  private LocalDate checkoutAt;
  private Map<Integer, Integer> suiteIdMapping;
}