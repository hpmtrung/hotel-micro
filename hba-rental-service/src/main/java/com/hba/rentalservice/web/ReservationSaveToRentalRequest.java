package com.hba.rentalservice.web;

import com.hba.partner.api.CustomerDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationSaveToRentalRequest {

  protected Integer rentalDiscountAmount;

  @NotNull
  @Size(min = 1)
  protected List<SuiteMapping> suiteMappings;

  public boolean isCustomersAllocatedValid() {
    final Set<String> personalIdSet = new HashSet<>();
    for (var suiteMapping : suiteMappings) {
      for (var roomMapping : suiteMapping.getRoomMappings()) {
        for (var customer : roomMapping.getCustomers()) {
          if (personalIdSet.contains(customer.getPersonalId())) return false;
          personalIdSet.add(customer.getPersonalId());
        }
      }
    }
    return true;
  }

  public List<CustomerDTO> getAllCustomers() {
    List<CustomerDTO> customers = new ArrayList<>();
    suiteMappings.stream()
        .map(SuiteMapping::getRoomMappings)
        .forEach(
            roomMappings ->
                roomMappings.stream().map(RoomMapping::getCustomers).forEach(customers::addAll));
    return customers;
  }

  @Getter
  @Setter
  @ToString
  public static class SuiteMapping {
    @NotNull private Integer suiteId;

    @Size(min = 1)
    @NotNull
    private List<RoomMapping> roomMappings;
  }

  @Getter
  @Setter
  @ToString
  public static class RoomMapping {

    @NotNull private Integer roomId;

    @NotNull
    @Size(min = 1)
    private List<CustomerDTO> customers;
  }
}