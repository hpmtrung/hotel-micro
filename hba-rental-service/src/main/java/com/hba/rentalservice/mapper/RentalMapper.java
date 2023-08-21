package com.hba.rentalservice.mapper;

import com.hba.rentalservice.domains.rental.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RentalMapper {

  RentalDTO toRentalDTO(Rental entity);

  RentalDetailDTO toRentalDetailDTO(RentalDetail entity);

  @Mapping(target = "service", source = "service.name")
  ServiceUsageDTO toServiceUsageDTO(ServiceUsage entity);
}