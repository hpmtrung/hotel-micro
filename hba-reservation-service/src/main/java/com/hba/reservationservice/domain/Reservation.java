package com.hba.reservationservice.domain;

import com.hba.reservationservice.api.ReservationStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = SEQUENCE, generator = "reservation_id_seq")
  @SequenceGenerator(
      name = "reservation_id_seq",
      sequenceName = "reservation_id_seq",
      allocationSize = 1)
  private Integer id;

  @Column(name = "personal_id")
  private String personalId;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(
      name = "personal_id",
      referencedColumnName = "personalId",
      insertable = false,
      updatable = false)
  private Customer owner;

  private LocalDateTime createdAt;

  private Integer depositPercent;

  @Enumerated(EnumType.STRING)
  private Paymethod paymethod;

  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  private LocalDate checkinAt;

  private LocalDate checkoutAt;

  private Integer timeoutDay;

  private Integer employeeId;

  private Integer total;

  @OneToMany(
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL,
      mappedBy = "reservation",
      orphanRemoval = true)
  private Set<ReservationDetail> details = new HashSet<>();

  public void addDetail(ReservationDetail detail) {
    details.add(detail);
    detail.setReservation(this);
  }

  public void computeAndSetTotal() {
    this.total = details.stream().mapToInt(ReservationDetail::getSubTotal).sum();
  }

  public enum Paymethod {
    BANKING,
    CASH,
    PAYPAL,
    VISA
  }
}