package com.fs.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FileVersionBlock implements Serializable {

  private static final long serialVersionUID = 39439236379658764L;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "file_version_id", referencedColumnName = "id")
  FileVersion fileVersion;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "block_id", referencedColumnName = "id")
  Block block;

  @Id @NotNull Integer orderNumber;
}