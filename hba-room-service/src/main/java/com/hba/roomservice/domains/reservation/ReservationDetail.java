package com.hba.roomservice.domains.reservation;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationDetail implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id @EqualsAndHashCode.Include
  @Column(name = "reservation_id")
  private Integer reservationId;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "reservation_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  private Reservation reservation;

  @Id @EqualsAndHashCode.Include private Integer suiteId;

  private Integer roomNum;
}