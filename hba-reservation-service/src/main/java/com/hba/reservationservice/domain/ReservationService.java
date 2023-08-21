package com.hba.reservationservice.domain;

import com.hba.common.messaging.DomainEvent;
import com.hba.common.saga.SagaException;
import com.hba.common.saga.SagaManager;
import com.hba.hbacore.model.User;
import com.hba.reservationservice.api.ReservationDTO;
import com.hba.reservationservice.mapper.ReservationMapper;
import com.hba.reservationservice.sagas.createtemp.CreateTempReservationSaga;
import com.hba.reservationservice.sagas.createtemp.CreateTempReservationSagaState;
import com.hba.reservationservice.sagas.paytemp.PayDepositTempReservationSaga;
import com.hba.reservationservice.sagas.paytemp.PayDepositTempReservationSagaState;
import com.hba.reservationservice.web.TempReservationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static com.hba.reservationservice.api.ReservationStatus.COMPLETE_DEPOSIT;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final CustomerService customerService;
  private final CreateTempReservationSaga createTempReservationSaga;
  private final PayDepositTempReservationSaga payDepositTempReservationSaga;
  private final SagaManager sagaManager;
  private final ReservationMapper reservationMapper;
  private final KafkaTemplate<Integer, DomainEvent> kafkaTemplate;

  public ReservationDTO createTempReservation(TempReservationCreateRequest request, User user) {
    String customerId = null;
    if (user != null) {
      customerId =
          customerService.findCustomerOfUser(user).map(Customer::getPersonalId).orElse(null);
    }

    CreateTempReservationSagaState sagaState = new CreateTempReservationSagaState();
    sagaState.setCustomerId(customerId);
    sagaState.setCheckinAt(request.getCheckinAt());
    sagaState.setCheckoutAt(request.getCheckoutAt());
    sagaState.setCreatedAt(LocalDateTime.now());
    sagaState.setDepositPercent(30);
    sagaState.setSuiteIdMapping(request.getSuiteIdMapping());

    try {
      sagaState = sagaManager.persistSagaInstance(createTempReservationSaga, sagaState);
    } catch (SagaException e) {
      Integer id = ((CreateTempReservationSagaState) e.getState()).getReservationId();
      if (id != null) {
        reservationRepository.deleteById(id);
      }

      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    return getReservationInfo(sagaState.getReservationId());
  }

  @Transactional(readOnly = true)
  public Reservation getReservationById(int reservationId) {
    return reservationRepository
        .findById(reservationId)
        .orElseThrow(() -> new ReservationNotFoundException(reservationId));
  }

  @Transactional(readOnly = true)
  public ReservationDTO getReservationInfo(int reservationId) {
    return reservationMapper.toDto(getReservationById(reservationId));
  }

  @Transactional
  public ReservationDTO completeTempReservation(int reservationId) {
    Reservation reservation = getReservationById(reservationId);
    // skip verifying reservation is temporary
    // ...
    reservation.setStatus(COMPLETE_DEPOSIT);

    reservationRepository.save(reservation);

    PayDepositTempReservationSagaState sagaState =
        new PayDepositTempReservationSagaState(reservationId);
    try {
      sagaManager.persistSagaInstance(payDepositTempReservationSaga, sagaState);
    } catch (SagaException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    return getReservationInfo(reservationId);
  }
}