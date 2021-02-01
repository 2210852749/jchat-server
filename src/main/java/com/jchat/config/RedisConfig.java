package com.jchat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisMessageListenerContainer redisMessageListenerContainer(
      @Autowired RedisConnectionFactory redisConnectionFactory) {
    RedisMessageListenerContainer redisMessageListenerContainer =
        new RedisMessageListenerContainer();
    redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
    return redisMessageListenerContainer;
  }
}
