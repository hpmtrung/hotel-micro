package com.hba.roomservice.domains.reservation;

import com.hba.reservationservice.api.ReservationDTO;
import com.hba.roomservice.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final EntityManager entityManager;
  private final ReservationMapper reservationMapper;

  public void persistReservation(ReservationDTO reservationDto) {
    Reservation reservation = reservationMapper.toEntity(reservationDto);
    log.info("Persisting replicated reservation {}", reservation);
    // reservationRepository.save(reservation);
    entityManager.persist(reservation);
    reservation.getDetails().forEach(entityManager::persist);
    entityManager.flush();
  }

}