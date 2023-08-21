package com.hba.reservationservice.domain;

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

  @Id
  @ToString.Exclude
  @EqualsAndHashCode.Include
  @ManyToOne
  @JoinColumn(name = "reservation_id", referencedColumnName = "id")
  private Reservation reservation;

  @Id
  @EqualsAndHashCode.Include
  @Column(name = "suite_id")
  private Integer suiteId;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "suite_id", referencedColumnName = "id", updatable = false, insertable = false)
  private Suite suite;

  private Integer roomNum;

  private Integer suitePrice;

  private Integer subTotal;

  private String discountId;

  public void computeAndSetSubTotal() {
    this.subTotal = suitePrice * roomNum;
  }
}