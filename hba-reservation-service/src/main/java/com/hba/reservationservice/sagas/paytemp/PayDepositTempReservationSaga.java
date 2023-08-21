package com.hba.reservationservice.sagas.paytemp;

import com.hba.common.messaging.CommandMessage;
import com.hba.common.saga.SagaDefinition;
import com.hba.common.saga.SagaManager;
import com.hba.common.saga.SimpleSaga;
import com.hba.reservationservice.api.ReservationServiceChannels;
import com.hba.reservationservice.domain.Reservation;
import com.hba.reservationservice.domain.ReservationNotFoundException;
import com.hba.reservationservice.domain.ReservationRepository;
import com.hba.roomservice.api.RoomServiceChannels;
import com.hba.roomservice.api.domain.TempReservationPayDepositCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hba.reservationservice.api.ReservationStatus.TEMPORARY;

@Service
@Slf4j
public class PayDepositTempReservationSaga
    implements SimpleSaga<PayDepositTempReservationSagaState> {

  private final ReservationRepository reservationRepository;

  public PayDepositTempReservationSaga(
      SagaManager sagaManager, ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
    sagaManager.registerSaga(this);
  }

  @Override
  public SagaDefinition<PayDepositTempReservationSagaState> getSagaDefinition() {
    return new SagaDefinition.Builder<PayDepositTempReservationSagaState>()
        .step()
        .onInvoke(this::createReservationPayDepositCommand)
        .onCompensation(this::createReservationPayDepositCompensationCommand)
        .build();
  }

  @Override
  public String getName() {
    return "payDepositTempReservationSaga";
  }

  @Override
  public String getInChannel() {
    return ReservationServiceChannels.RESERVATION_PAY_DEPOSIT_COMMAND_CHANNEL;
  }

  @Transactional
  public CommandMessage<TempReservationPayDepositCommand>
      createReservationPayDepositCompensationCommand(PayDepositTempReservationSagaState state) {
    final int reservationId = state.getReservationId();
    Reservation reservation =
        reservationRepository
            .findById(state.getReservationId())
            .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    reservation.setStatus(TEMPORARY);
    reservationRepository.save(reservation);

    return CommandMessage.of(new TempReservationPayDepositCommand(reservationId, false))
        .to(RoomServiceChannels.COMMAND_CHANNEL);
  }

  private CommandMessage<TempReservationPayDepositCommand> createReservationPayDepositCommand(
      PayDepositTempReservationSagaState state) {
    return CommandMessage.of(new TempReservationPayDepositCommand(state.getReservationId(), true))
        .to(RoomServiceChannels.COMMAND_CHANNEL);
  }
}