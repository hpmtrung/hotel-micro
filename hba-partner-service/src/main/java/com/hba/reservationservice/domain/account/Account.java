package com.hba.reservationservice.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hba.hbacore.web.CustomImageUrlSerializer;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Account implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "tai_khoan_id_seq")
  @SequenceGenerator(name = "tai_khoan_id_seq", sequenceName = "tai_khoan_id_seq", allocationSize = 1)
  private Integer id;

  @NaturalId
  private String email;

  @JsonIgnore
  @ToString.Exclude
  private String password;

  private String name;

  @Enumerated(value = EnumType.STRING)
  private Authority authority;

  private Boolean activated;

  @JsonSerialize(using = CustomImageUrlSerializer.class)
  private String imageUrl;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Account)) return false;
    Account account = (Account) o;
    return Objects.equals(email, account.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }

}