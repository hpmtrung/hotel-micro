package com.fs.domain;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client implements UserDetails {

  private static final long serialVersionUID = 854588680178312543L;

  @EqualsAndHashCode.Include @Id @GeneratedValue UUID id;

  @NotNull String username;

  @NotNull String password;

  @NotNull String secretKey;

  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", orphanRemoval = true)
  Set<Workspace> workspaces = new HashSet<>();

  public void addWorkspace(Workspace ws) {
    workspaces.add(ws);
    ws.setOwner(this);
  }

  public void removeWorkspace(Workspace ws) {
    workspaces.remove(ws);
    ws.setOwner(null);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}