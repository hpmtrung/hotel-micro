package com.hba.reservationservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hba.hbacore.web.CustomImageUrlSerializer;
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
public class Suite implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id @EqualsAndHashCode.Include private Integer id;

  private Integer typeId;

  private String typeName;

  private Integer styleId;

  private String styleName;

  private Integer area;

  @JsonSerialize(using = CustomImageUrlSerializer.class)
  private String imageUrl;

  private Integer occupation;
}