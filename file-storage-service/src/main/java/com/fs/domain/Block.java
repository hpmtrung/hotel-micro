package com.fs.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Block implements Serializable {

  private static final long serialVersionUID = -4583743876110854637L;

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "block_id_seq")
  @SequenceGenerator(name = "block_id_seq", sequenceName = "block_id_seq", allocationSize = 1)
  Integer id;

  @NaturalId @EqualsAndHashCode.Include String checksum;

  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "block", orphanRemoval = true)
  Set<FileVersionBlock> versions = new HashSet<>();

  public Block(String checksum) {
    this.checksum = checksum;
  }
}