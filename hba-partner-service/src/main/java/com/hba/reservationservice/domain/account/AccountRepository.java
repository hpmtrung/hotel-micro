package com.hba.reservationservice.domain.account;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {
  Optional<Account> findByEmail(String email);
}