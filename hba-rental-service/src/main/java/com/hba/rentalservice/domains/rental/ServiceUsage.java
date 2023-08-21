package com.hba.rentalservice.domains.rental;

import com.hba.rentalservice.domains.service.HService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceUsage implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = SEQUENCE, generator = "service_usage_id_seq")
  @SequenceGenerator(
      name = "service_usage_id_seq",
      sequenceName = "service_usage_id_seq",
      allocationSize = 1)
  private Integer id;

  private Integer invoiceId;

  private LocalDateTime createdAt;

  private Integer amount;

  private Integer servicePrice;

  private Integer total;

  private Boolean paid;

  private String employeeId;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rental_detail_id", referencedColumnName = "id", nullable = false)
  private RentalDetail rentalDetail;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_id", referencedColumnName = "id", nullable = false)
  private HService service;
}