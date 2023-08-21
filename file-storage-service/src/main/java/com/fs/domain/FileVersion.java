package com.fs.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileVersion implements Serializable {

  private static final long serialVersionUID = 5988402329119427800L;

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "file_version_id_seq")
  @SequenceGenerator(
      name = "file_version_id_seq",
      sequenceName = "file_version_id_seq",
      allocationSize = 1)
  Integer id;

  Integer versionNumber;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id", referencedColumnName = "id")
  File file;

  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "fileVersion", orphanRemoval = true)
  Set<FileVersionBlock> blocks = new HashSet<>();

  public void addBlock(Block block, int orderNumber) {
    FileVersionBlock versionBlock = new FileVersionBlock(this, block, orderNumber);
    blocks.add(versionBlock);
    block.getVersions().add(versionBlock);
  }

  public void removeBlock(Block block, int orderNumber) {
    FileVersionBlock versionBlock = new FileVersionBlock(this, block, orderNumber);
    block.getVersions().remove(versionBlock);
    blocks.remove(versionBlock);
    versionBlock.setBlock(null);
    versionBlock.setFileVersion(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FileVersion)) return false;

    FileVersion that = (FileVersion) o;

    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return FileVersion.class.hashCode();
  }
}