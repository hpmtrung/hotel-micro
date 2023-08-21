package com.hba.roomservice.messaging;

import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.Message;
import com.hba.common.saga.SagaCommandDispatcherFactory;
import com.hba.common.saga.SagaCommandHandlers;
import com.hba.reservationservice.api.TempReservationCreateCommand;
import com.hba.reservationservice.api.TempReservationVerifyReply;
import com.hba.reservationservice.api.TempReservationVerifyReply.DetailInfo;
import com.hba.roomservice.api.RoomServiceChannels;
import com.hba.roomservice.api.domain.TempReservationPayDepositCommand;
import com.hba.roomservice.api.domain.TempReservationVerifyCommand;
import com.hba.roomservice.domains.reservation.Reservation;
import com.hba.roomservice.domains.reservation.ReservationRepository;
import com.hba.roomservice.domains.reservation.ReservationService;
import com.hba.roomservice.domains.room.RoomService;
import com.hba.roomservice.api.domain.SuiteDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.hba.reservationservice.api.ReservationStatus.COMPLETE_DEPOSIT;
import static com.hba.reservationservice.api.ReservationStatus.TEMPORARY;

@Slf4j
@Component
public class RoomCommandHandlers {

  private final RoomService roomService;
  private final ReservationRepository reservationRepository;
  private final ReservationService reservationService;

  public RoomCommandHandlers(
      SagaCommandDispatcherFactory commandDispatcherFactory,
      RoomService roomService,
      ReservationRepository reservationRepository,
      ReservationService reservationService) {
    this.roomService = roomService;
    this.reservationRepository = reservationRepository;
    this.reservationService = reservationService;

    SagaCommandHandlers handlers =
        SagaCommandHandlers.fromChannel(RoomServiceChannels.COMMAND_CHANNEL)
            .onMessage(TempReservationVerifyCommand.class, this::verifyTempReservation)
            .onMessage(TempReservationPayDepositCommand.class, this::updateReservationPayDeposit)
            .onMessage(
                TempReservationCreateCommand.class, this::handleTempReservationCreatedCommand)
            .build();

    commandDispatcherFactory.create("roomCommandHandlers", handlers);
  }

  public Message handleTempReservationCreatedCommand(
      CommandMessage<TempReservationCreateCommand> message) {
    log.info("Replicating new reservation with command {}", message);
    try {
      reservationService.persistReservation(message.getCommand().getReservation());
    } catch (RuntimeException e) {
      return Message.withFailure();
    }
    return Message.withSuccess();
  }

  private Message updateReservationPayDeposit(
      CommandMessage<TempReservationPayDepositCommand> message) {
    log.info("Update state pay deposit for reservation with command {}", message);

    final int reservationId = message.getCommand().getReservationId();
    Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

    final boolean accepted = message.getCommand().isAccept();
    if (reservation == null) {
      return Message.withFailure();
    } else {
      if (accepted) {
        reservation.setStatus(COMPLETE_DEPOSIT);
      } else {
        reservation.setStatus(TEMPORARY);
      }
      reservationRepository.save(reservation);
      return Message.withSuccess();
    }
  }

  private Message verifyTempReservation(CommandMessage<TempReservationVerifyCommand> message) {
    log.info("Verifying temp reservation with command {}", message);

    TempReservationVerifyCommand command = message.getCommand();
    if (roomService.isRentableSuiteMappingValid(
        command.getCheckinAt(), command.getCheckoutAt(), command.getSuiteIdMapping())) {
      Map<Integer, DetailInfo> infos = new HashMap<>();

      for (Map.Entry<Integer, Integer> entry : command.getSuiteIdMapping().entrySet()) {
        int suiteId = entry.getKey();
        SuiteDTO suite = roomService.getSuiteInfo(suiteId);
        infos.put(
            suiteId,
            new DetailInfo(
                suite.getOriginalPrice(),
                suite.getDiscount() != null ? suite.getDiscount().getId() : null));
      }
      return Message.withSuccess(new TempReservationVerifyReply(infos));
    } else {
      return Message.withFailure("Suite id mapping is invalid");
    }
  }
}