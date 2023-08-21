package com.hba.reservationservice.domain.employee;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Immutable
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Department implements Serializable {

  private static final long serialVersionUID = 6341666794901279659L;

  @Id
  @EqualsAndHashCode.Include
  private Integer id;

  private String name;

}