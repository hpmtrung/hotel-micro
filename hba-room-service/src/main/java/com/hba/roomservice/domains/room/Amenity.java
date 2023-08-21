package com.hba.roomservice.domains.room;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Immutable
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Amenity implements Serializable {

  private static final long serialVersionUID = 7327001442216871225L;

  @Id
  @EqualsAndHashCode.Include
  private Integer id;

  private String name;

}