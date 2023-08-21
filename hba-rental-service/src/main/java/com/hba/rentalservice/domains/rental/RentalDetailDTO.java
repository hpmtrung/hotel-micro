package com.hba.rentalservice.domains.rental;

import com.hba.partner.api.CustomerDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
public class RentalDetailDTO {
  private int id;
  private int roomId;
  private int roomPrice;
  private LocalDateTime checkinAt;
  private LocalDateTime checkoutAt;
  private int total;
  private Integer discountId;
  private Set<CustomerDTO> customers;
  private Set<ServiceUsageDTO> serviceUsages;
}