package com.hba.reservationservice.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

  Optional<Customer> findByAccountId(int accountId);
}