package com.fs.config;

import com.fs.ApplicationProperties;
import com.fs.services.BlockEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.ftp.dsl.Ftp;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.Executors;

import static org.springframework.integration.file.remote.gateway.AbstractRemoteFileOutboundGateway.Command.RM;

@Slf4j
@Configuration
public class FTPIntegrationConfig {

  @Bean
  public BlockEncryptor blockEncryptor(ApplicationProperties properties) {
    return new BlockEncryptor(properties.getFtp().getAesKey());
  }

  @Bean
  public DefaultFtpSessionFactory ftpSessionFactory(ApplicationProperties properties) {
    DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
    sf.setHost(properties.getFtp().getHost());
    sf.setPort(properties.getFtp().getPort());
    sf.setUsername(properties.getFtp().getUserName());
    sf.setPassword(properties.getFtp().getPassword());
    sf.setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE);
    return sf;
  }

  @Bean
  public MessageChannel toFtpChannel() {
    return MessageChannels.executor(Executors.newFixedThreadPool(3)).get();
  }

  @Bean
  public MessageChannel deleteFtpChannel() {
    return MessageChannels.executor(Executors.newFixedThreadPool(3)).get();
  }

  @Bean
  public IntegrationFlow ftpOutboundFlow(DefaultFtpSessionFactory sessionFactory) {
    return IntegrationFlows.from("toFtpChannel")
        .handle(
            Ftp.outboundAdapter(sessionFactory, FileExistsMode.IGNORE)
                .useTemporaryFileName(false)
                .fileNameExpression("headers['file_name']")
                .remoteDirectory("files/file-storage"))
        .get();
  }

  @Bean
  public IntegrationFlow ftpDeleteFlow(DefaultFtpSessionFactory sessionFactory) {
    return IntegrationFlows.from("deleteFtpChannel")
        .handle(Ftp.outboundGateway(sessionFactory, RM, "'files/file-storage/' + payload"))
        .handle(
            msg ->
                log.info(
                    "Deleting a file [status={}, name={}]",
                    ((boolean) msg.getPayload()) ? "Success" : "Fail",
                    msg.getHeaders().get("file_remoteFile")))
        .get();
  }

  @Bean
  public FtpRemoteFileTemplate template(SessionFactory<FTPFile> sf) {
    return new FtpRemoteFileTemplate(sf);
  }
}