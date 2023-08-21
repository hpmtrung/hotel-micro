package com.hba.rentalservice.domains.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HServiceService {

  private final HServiceRepository serviceRepository;

  @Transactional(readOnly = true)
  public List<HService> getServices() {
    return serviceRepository.findAll();
  }

}