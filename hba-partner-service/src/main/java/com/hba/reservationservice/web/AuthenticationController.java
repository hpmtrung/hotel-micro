package com.hba.reservationservice.web;

import com.hba.hbacore.model.User;
import com.hba.reservationservice.model.CredentialDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authenticate")
public class AuthenticationController {

  private final ProviderManager providerManager;

  @PostMapping
  User authenticate(@Valid @RequestBody CredentialDTO credential) {
    log.info("Authenticating with credential {}", credential);
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword());
    Authentication authentication = providerManager.authenticate(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return (User) authentication.getPrincipal();
  }
}