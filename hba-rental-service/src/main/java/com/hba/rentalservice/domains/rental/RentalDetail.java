package com.hba.rentalservice.domains.rental;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
public class RentalDetail implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = SEQUENCE, generator = "rental_detail_id_seq")
  @SequenceGenerator(
      name = "rental_detail_id_seq",
      sequenceName = "rental_detail_id_seq",
      allocationSize = 1)
  private Integer id;

  private Integer invoiceId;

  @NotNull private Integer roomId;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "rental_id", referencedColumnName = "id")
  private Rental rental;

  private Integer roomPrice;

  private LocalDateTime checkinAt;

  private LocalDateTime checkoutAt;

  private Integer total;

  private Integer discountId;

  @Version private Integer version;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "customer_room",
      joinColumns = @JoinColumn(name = "rental_detail_id"),
      inverseJoinColumns = @JoinColumn(name = "personal_id"))
  private Set<String> personalIds = new HashSet<>();

  @OneToMany(mappedBy = "rentalDetail", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ServiceUsage> serviceUsages = new HashSet<>();
}