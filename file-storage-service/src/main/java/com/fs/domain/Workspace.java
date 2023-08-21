package com.fs.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
public class Workspace implements Serializable {

  private static final long serialVersionUID = 2476402477033016829L;

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "workspace_id_seq")
  @SequenceGenerator(
      name = "workspace_id_seq",
      sequenceName = "workspace_id_seq",
      allocationSize = 1)
  Integer id;

  @NotNull String name;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", referencedColumnName = "id")
  Client owner;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Workspace)) return false;
    Workspace workspace = (Workspace) o;
    return Objects.equals(id, workspace.id);
  }

  @Override
  public int hashCode() {
    return Workspace.class.hashCode();
  }

}