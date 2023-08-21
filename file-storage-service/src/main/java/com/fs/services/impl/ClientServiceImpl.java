package com.fs.services.impl;

import com.fs.domain.Client;
import com.fs.model.ClientCredential;
import com.fs.model.ClientDTO;
import com.fs.model.ClientRegister;
import com.fs.repositories.ClientRepository;
import com.fs.security.TokenProvider;
import com.fs.services.ClientService;
import com.fs.services.exceptions.BadCredentialException;
import com.fs.services.exceptions.UsernameUniqueException;
import com.fs.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final ClientRepository clientRepository;

  @Override
  public ClientDTO registerClient(ClientRegister client) {
    if (clientRepository.existsByUsername(client.getUsername())) {
      throw new UsernameUniqueException();
    }

    Client entity = new Client();
    entity.setId(UUID.randomUUID());
    entity.setPassword(passwordEncoder.encode(client.getPassword()));
    entity.setUsername(client.getUsername());
    entity.setSecretKey(RandomUtil.generateAlphanumeric(40));

    entity = clientRepository.save(entity);
    return new ClientDTO(entity.getId(), entity.getUsername(), entity.getSecretKey());
  }

  @Override
  public String authenticate(ClientCredential credential) {
    Client client =
        clientRepository
            .findByIdAndSecretKey(UUID.fromString(credential.getId()), credential.getSecret())
            .orElseThrow(BadCredentialException::new);

    return tokenProvider.createToken(client);
  }
}