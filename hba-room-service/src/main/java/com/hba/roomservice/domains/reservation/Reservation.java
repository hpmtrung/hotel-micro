package com.hba.roomservice.domains.reservation;

import com.hba.reservationservice.api.ReservationStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id @EqualsAndHashCode.Include private Integer id;

  private LocalDateTime createdAt;

  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  private LocalDate checkinAt;

  private LocalDate checkoutAt;

  private Integer timeoutDay;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservation", orphanRemoval = true)
  private Set<ReservationDetail> details = new HashSet<>();
}