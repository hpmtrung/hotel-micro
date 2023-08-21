package com.hba.reservationservice.sagas.createtemp;

import com.hba.common.messaging.CommandMessage;
import com.hba.common.saga.ApiReplyFaultException;
import com.hba.common.saga.SagaDefinition;
import com.hba.common.saga.SagaManager;
import com.hba.common.saga.SimpleSaga;
import com.hba.reservationservice.api.ReservationDTO;
import com.hba.reservationservice.api.ReservationServiceChannels;
import com.hba.reservationservice.api.TempReservationCreateCommand;
import com.hba.reservationservice.api.TempReservationVerifyReply;
import com.hba.reservationservice.api.TempReservationVerifyReply.DetailInfo;
import com.hba.reservationservice.domain.Reservation;
import com.hba.reservationservice.domain.ReservationDetail;
import com.hba.reservationservice.domain.ReservationRepository;
import com.hba.reservationservice.mapper.ReservationMapper;
import com.hba.roomservice.api.RoomServiceChannels;
import com.hba.roomservice.api.domain.TempReservationVerifyCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.hba.reservationservice.api.ReservationStatus.TEMPORARY;

@Slf4j
@Service
public class CreateTempReservationSaga implements SimpleSaga<CreateTempReservationSagaState> {

  private final ReservationMapper reservationMapper;
  private final ReservationRepository reservationRepository;

  public CreateTempReservationSaga(
      SagaManager sagaManager,
      ReservationMapper reservationMapper,
      ReservationRepository reservationRepository) {
    this.reservationMapper = reservationMapper;
    this.reservationRepository = reservationRepository;
    sagaManager.registerSaga(this);
  }

  @Override
  public SagaDefinition<CreateTempReservationSagaState> getSagaDefinition() {
    return new SagaDefinition.Builder<CreateTempReservationSagaState>()
        .step()
        .onInvoke(this::makeTempReservationVerifyCommand)
        .onReply(TempReservationVerifyReply.class, this::replyTempReservationVerifyCommand)
        .step()
        .onInvoke(this::makeReservationCreateCommand)
        .build();
  }

  @Transactional(readOnly = true)
  public CommandMessage<TempReservationCreateCommand> makeReservationCreateCommand(
      CreateTempReservationSagaState state) {
    int reservationId = state.getReservationId();
    ReservationDTO dto =
        reservationMapper.toDto(reservationRepository.findById(reservationId).orElseThrow());
    return CommandMessage.of(new TempReservationCreateCommand(dto))
        .to(RoomServiceChannels.COMMAND_CHANNEL);
  }

  @Override
  public String getName() {
    return "createTempReservationSaga";
  }

  @Override
  public String getInChannel() {
    return ReservationServiceChannels.RESERVATION_CREATE_COMMAND_CHANNEL;
  }

  private CommandMessage<TempReservationVerifyCommand> makeTempReservationVerifyCommand(
      CreateTempReservationSagaState state) {
    return CommandMessage.of(
            new TempReservationVerifyCommand(
                state.getCheckinAt(), state.getCheckoutAt(), state.getSuiteIdMapping()))
        .to(RoomServiceChannels.COMMAND_CHANNEL);
  }

  @Transactional
  public void replyTempReservationVerifyCommand(
      CreateTempReservationSagaState state, TempReservationVerifyReply payload) {
    log.info("Reply after verifying {}", payload);

    Reservation reservation = state.toReservation();
    Map<Integer, DetailInfo> suitePrices = payload.getSuiteInfos();
    for (Map.Entry<Integer, Integer> idMapping : state.getSuiteIdMapping().entrySet()) {
      int suiteId = idMapping.getKey();
      int roomNum = idMapping.getValue();

      if (!suitePrices.containsKey(suiteId)) {
        throw new ApiReplyFaultException("Suite id '" + suiteId + "' not found");
      }
      DetailInfo info = suitePrices.get(suiteId);

      ReservationDetail detail = new ReservationDetail();
      detail.setSuiteId(suiteId);
      detail.setRoomNum(roomNum);
      detail.setSuitePrice(info.getPrice());
      detail.setDiscountId(info.getDiscountId());
      detail.computeAndSetSubTotal();

      reservation.addDetail(detail);
    }

    reservation.setPaymethod(Reservation.Paymethod.PAYPAL);
    reservation.setStatus(TEMPORARY);
    reservation.setTimeoutDay(3);
    reservation.computeAndSetTotal();

    reservation = reservationRepository.save(reservation);

    state.setReservationId(reservation.getId());
  }
}