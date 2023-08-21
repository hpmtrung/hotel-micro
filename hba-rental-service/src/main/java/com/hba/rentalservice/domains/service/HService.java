package com.hba.rentalservice.domains.service;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Immutable
@Table(name = "Service")
@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HService {

  @Id @EqualsAndHashCode.Include private Integer id;

  private String name;

  private Integer unitPrice;

  @Version private Integer version;
}