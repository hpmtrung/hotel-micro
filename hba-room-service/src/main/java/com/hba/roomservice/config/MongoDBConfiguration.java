package com.hba.roomservice.config;

import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;

@Configuration
public class MongoDBConfiguration {

  @Bean
  public MongoTemplate mongoTemplate(MongoClient client) {
    MongoTemplate template = new MongoTemplate(client, "roomdb");
    template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
    return template;
  }
}