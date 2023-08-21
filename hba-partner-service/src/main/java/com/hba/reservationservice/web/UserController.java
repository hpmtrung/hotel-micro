package com.hba.reservationservice.web;

import com.hba.hbacore.model.User;
import com.hba.reservationservice.domain.account.Account;
import com.hba.reservationservice.domain.account.AccountService;
import com.hba.reservationservice.domain.account.UserInfoUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final AccountService accountService;

  @GetMapping("/info")
  Account getUserInfo(@AuthenticationPrincipal User user) {
    log.info("Get user info from {}", user);
    return accountService.findById(user.getId());
  }

  @PutMapping("/info_update")
  ResponseEntity<Object> updateInfo(
      @AuthenticationPrincipal User user,
      @RequestPart(required = false) UserInfoUpdate infoUpdate,
      @RequestPart(required = false) MultipartFile image) {
    log.info(
        "Update user info from {} with dto update {} and image {}",
        user,
        infoUpdate,
        image != null ? image.getOriginalFilename() : null);
    if (infoUpdate == null && image == null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(accountService.updateAccount(user, infoUpdate, image));
  }
}