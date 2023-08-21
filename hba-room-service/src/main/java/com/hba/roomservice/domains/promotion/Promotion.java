package com.hba.roomservice.domains.promotion;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hba.hbacore.web.CustomImageUrlSerializer;
import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Immutable
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Promotion {

  @Id @ToString.Include @EqualsAndHashCode.Include private Integer id;

  private String title;

  @ToString.Include
  @JsonSerialize(using = CustomImageUrlSerializer.class)
  private String imageUrl;

  private String description;

  private String content;
}