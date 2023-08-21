package com.fs.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class File implements Serializable {

  private static final long serialVersionUID = -5453360461077132815L;

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "file_id_seq")
  @SequenceGenerator(name = "file_id_seq", sequenceName = "file_id_seq", allocationSize = 1)
  Integer id;

  @NotNull String name;

  @NotNull String relPath;

  @Version int latestVersion;

  @NotNull String checksum;

  @NotNull LocalDateTime createdAt;

  @NotNull LocalDateTime lastModified;

  String metadata;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "workspace_id", referencedColumnName = "id")
  Workspace workspace;

  @ToString.Exclude
  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "file",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  Set<FileVersion> versions = new HashSet<>();

  public File(
      String name,
      String relPath,
      int latestVersion,
      String checksum,
      LocalDateTime createdAt,
      LocalDateTime lastModified,
      Workspace workspace) {
    this.name = name;
    this.relPath = relPath;
    this.latestVersion = latestVersion;
    this.checksum = checksum;
    this.createdAt = createdAt;
    this.lastModified = lastModified;
    this.workspace = workspace;
  }

  public void addFileVersion(FileVersion fileVersion) {
    versions.add(fileVersion);
    fileVersion.setFile(this);
  }

  public void removeFileVersion(FileVersion fileVersion) {
    versions.remove(fileVersion);
    fileVersion.setFile(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof File)) return false;

    File file = (File) o;

    return Objects.equals(id, file.id);
  }

  @Override
  public int hashCode() {
    return File.class.hashCode();
  }

}