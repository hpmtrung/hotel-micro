package com.hba.rentalservice.controllers;

import com.hba.rentalservice.domains.service.HService;
import com.hba.rentalservice.domains.service.HServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service")
public class ServiceController {

  private final HServiceService hServiceService;

  @GetMapping
  List<HService> getServices() {
    log.info("Getting services");
    return hServiceService.getServices();
  }
}