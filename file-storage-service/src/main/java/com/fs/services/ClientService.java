package com.fs.services;

import com.fs.domain.Client;
import com.fs.model.ClientCredential;
import com.fs.model.ClientDTO;
import com.fs.model.ClientRegister;

public interface ClientService {

  ClientDTO registerClient(ClientRegister client);

  String authenticate(ClientCredential credential);

}