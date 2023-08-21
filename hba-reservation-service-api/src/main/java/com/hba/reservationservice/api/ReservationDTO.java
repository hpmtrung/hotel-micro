package com.hba.reservationservice.api;

import com.hba.partner.api.CustomerDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationDTO {
  private Integer id;
  private CustomerDTO owner;
  private LocalDateTime createdAt;
  private LocalDate checkinAt;
  private LocalDate checkoutAt;
  private Integer depositPercent;
  private String paymethod;
  private String status;
  private String employeeId;
  private Integer total;
  private Integer timeoutDay;
  private List<ReservationDetailDTO> details;
}