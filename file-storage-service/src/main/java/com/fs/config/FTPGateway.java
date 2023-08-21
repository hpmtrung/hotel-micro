package com.fs.config;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway
public interface FTPGateway {

  @Gateway(requestChannel = "toFtpChannel")
  void sendBlocks(Message<byte[]> message);

  @Gateway(requestChannel = "deleteFtpChannel")
  void deleteBlock(String fileName);
}