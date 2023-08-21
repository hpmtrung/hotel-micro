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
public class SuiteStyle implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @EqualsAndHashCode.Include
  private Integer id;

  private String name;

  private Integer occupation;

}