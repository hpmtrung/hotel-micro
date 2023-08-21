package com.hba.reservationservice.mapper;

import com.hba.reservationservice.api.ReservationDTO;
import com.hba.reservationservice.api.ReservationDetailDTO;
import com.hba.reservationservice.domain.Reservation;
import com.hba.reservationservice.domain.ReservationDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

  ReservationDetailDTO toDetailDto(ReservationDetail entity);

  ReservationDTO toDto(Reservation entity);
}