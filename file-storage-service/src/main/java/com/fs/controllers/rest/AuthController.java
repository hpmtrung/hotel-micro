package com.fs.controllers.rest;

import com.fs.model.ClientCredential;
import com.fs.model.ClientDTO;
import com.fs.model.ClientRegister;
import com.fs.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {

  private final ClientService clientService;

  @PostMapping
  String authenticate(@RequestBody ClientCredential credential) {
    log.info("Authenticating {}", credential);
    return clientService.authenticate(credential);
  }

  @PostMapping("/register")
  ClientDTO registerClient(@RequestBody ClientRegister client) {
    log.info("Registering {}", client);
    return clientService.registerClient(client);
  }
}