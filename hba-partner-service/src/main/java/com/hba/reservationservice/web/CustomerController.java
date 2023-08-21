package com.hba.reservationservice.web;

import com.hba.hbacore.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {

  @GetMapping("/info")
  String getCusomerInfo(@AuthenticationPrincipal User user) {
    log.info("logging {}", user);
    return "hello";
  }
}