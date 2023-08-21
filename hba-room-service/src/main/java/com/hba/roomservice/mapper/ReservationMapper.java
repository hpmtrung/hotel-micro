package com.hba.roomservice.mapper;

import com.hba.reservationservice.api.ReservationDTO;
import com.hba.reservationservice.api.ReservationDetailDTO;
import com.hba.roomservice.domains.reservation.Reservation;
import com.hba.roomservice.domains.reservation.ReservationDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

  Reservation toEntityHelper(ReservationDTO dto);

  @Mapping(target = "suiteId", source = "suite.id")
  ReservationDetail toDetailEntity(ReservationDetailDTO dto);

  default Reservation toEntity(ReservationDTO dto) {
    Reservation reservation = toEntityHelper(dto);
    reservation.getDetails().forEach(d -> d.setReservationId(reservation.getId()));
    return reservation;
  }

}