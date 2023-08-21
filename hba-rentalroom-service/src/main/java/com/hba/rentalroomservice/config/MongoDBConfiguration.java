package com.hba.rentalroomservice.config;

import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDBConfiguration {
  @Bean
  public MongoClientFactoryBean mongo() {
    MongoClientFactoryBean mongo = new MongoClientFactoryBean();
    mongo.setHost("192.168.1.7");
    mongo.setPort(27017);
    return mongo;
  }

  @Bean
  public MongoTemplate mongoTemplate(MongoClient client) {
    return new MongoTemplate(client, "rentalroomdb");
  }
}