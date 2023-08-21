package com.hba.roomservice.domains.room;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hba.hbacore.web.CustomImageUrlSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Suite implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @EqualsAndHashCode.Include
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "type_id", referencedColumnName = "id")
  private SuiteType suiteType;

  @ManyToOne
  @JoinColumn(name = "style_id", referencedColumnName = "id")
  private SuiteStyle suiteStyle;

  private Integer area;

  private Integer price;

  @JsonSerialize(using = CustomImageUrlSerializer.class)
  private String imageUrl;

  private Integer rentNum;

  @Version
  private Integer version;

}