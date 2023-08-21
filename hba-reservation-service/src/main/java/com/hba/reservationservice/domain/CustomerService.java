package com.hba.reservationservice.domain;

import com.hba.hbacore.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;

  @Transactional(readOnly = true)
  public Optional<Customer> findCustomerOfUser(User user) {
    return customerRepository.findByAccountId(user.getId());
  }

}