package com.hba.reservationservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @EqualsAndHashCode.Include
  private String personalId;

  private String name;

  private String address;

  private String phoneNumber;

  private String taxCode;

  private Integer accountId;

}