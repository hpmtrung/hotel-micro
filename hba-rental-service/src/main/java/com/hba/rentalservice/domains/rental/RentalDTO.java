package com.hba.rentalservice.domains.rental;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
public class RentalDTO {
  private int id;
  private LocalDateTime createdAt;
  private LocalDateTime checkinAt;
  private LocalDateTime checkoutAt;
  private Integer reservationId;
  private Integer employeeId;
  private String personalId;
  private String status;
  private Set<RentalDetailDTO> details;
}