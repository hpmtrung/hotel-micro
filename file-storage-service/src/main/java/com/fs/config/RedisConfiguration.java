package com.fs.config;

import com.fs.repositories.BlockRepository;
import com.fs.repositories.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;

@EnableCaching
@Configuration
public class RedisConfiguration {
  @Bean
  public LettuceConnectionFactory redisConnectionFactory(
      @Value("${application.redis.host}") String host) {
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, 6379));
  }

  @Bean
  public RedisCacheConfiguration defaultCacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(1))
        .disableCachingNullValues();
  }

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.builder(connectionFactory)
        .enableStatistics()
        .cacheDefaults(defaultCacheConfiguration())
        .withCacheConfiguration(
            WorkspaceRepository.CACHE_BY_NAME_AND_OWNERID, defaultCacheConfiguration())
        .withCacheConfiguration(BlockRepository.CACHE_BY_CHECKSUM, defaultCacheConfiguration())
        .build();
  }
}