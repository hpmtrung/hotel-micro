package com.hba.rentalservice.domains.rental;

import com.hba.rentalservice.api.RentalStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rental implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = SEQUENCE, generator = "rental_id_seq")
  @SequenceGenerator(name = "rental_id_seq", sequenceName = "rental_id_seq", allocationSize = 1)
  private Integer id;

  private LocalDateTime createdAt;

  private LocalDateTime checkinAt;

  private LocalDateTime checkoutAt;

  private Integer reservationId;

  private String employeeId;

  private String personalId;

  @Enumerated(EnumType.STRING)
  private RentalStatus status;

  @OneToMany(
      mappedBy = "rental",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<RentalDetail> details = new HashSet<>();

  @Version
  private Integer version;
}