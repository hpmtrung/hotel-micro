package com.hba.roomservice.web;

import com.hba.roomservice.domains.promotion.Promotion;
import com.hba.roomservice.domains.promotion.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/promotion")
public class PromotionController {

  private final PromotionService promotionService;

  @GetMapping
  List<Promotion> getActivePromotions() {
    log.info("Get active promotions");
    return promotionService.getActivePromotions();
  }
}