package com.hba.roomservice.domains.promotion;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class PromotionService {

  private final PromotionRepository repository;

  @Transactional(readOnly = true)
  public List<Promotion> getActivePromotions() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }
}