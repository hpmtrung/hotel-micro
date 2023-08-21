package com.hba.hbacore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class User implements UserDetails {

  private static final long serialVersionUID = -3716846218162684715L;

  private int id;
  private String email;
  private String password;
  private String authority;

  public String getEmail() {
    return email;
  }

  public User setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getAuthority() {
    return authority;
  }

  public User setAuthority(String authority) {
    this.authority = authority;
    return this;
  }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(authority));
  }

  @Override
  @JsonIgnore
  public String getPassword() {
    return password;
  }

  public User setPassword(String password) {
    this.password = password;
    return this;
  }

  public int getId() {
    return id;
  }

  public User setId(int id) {
    this.id = id;
    return this;
  }

  @Override
  @JsonIgnore
  public String getUsername() {
    return email;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return "User{"
        + "id="
        + id
        + ", email='"
        + email
        + '\''
        + ", authority='"
        + authority
        + '\''
        + '}';
  }
}