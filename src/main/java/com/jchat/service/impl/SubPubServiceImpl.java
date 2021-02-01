package com.jchat.service.impl;

import com.jchat.service.SubPubService;
import com.jchat.util.SessionUtil;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class SubPubServiceImpl implements SubPubService {

  private final StringRedisTemplate stringRedisTemplate;
  private final Map<WebSocketSession, MessageListener> messageListenerMap;
  private final RedisMessageListenerContainer redisMessageListenerContainer;

  public SubPubServiceImpl(StringRedisTemplate stringRedisTemplate,
      RedisMessageListenerContainer redisMessageListenerContainer) {
    this.stringRedisTemplate = stringRedisTemplate;
    this.messageListenerMap = new ConcurrentHashMap<>();
    this.redisMessageListenerContainer = redisMessageListenerContainer;
  }

  @Override
  public void pub(WebSocketSession session, TextMessage textMessage) {
    // 向 session 的 sub key 发送消息
    stringRedisTemplate.convertAndSend(genSubKey(session), textMessage.getPayload());
  }

  @Override
  public void sub(WebSocketSession session) {
    // 使用 redis sub 功能实现
    MessageListener messageListener = (message, pattern) -> {
      log.info("Redis sub receive: [{}]", new String(message.getBody()));
      try {
        session.sendMessage(new TextMessage(message.getBody()));
      } catch (IOException e) {
        log.error("", e);
      }
    };
    redisMessageListenerContainer.addMessageListener(messageListener,
        new ChannelTopic(genSubKey(session)));
    messageListenerMap.put(session, messageListener);
  }

  @Override
  public void unSub(WebSocketSession session) {
    // 取消订阅
    redisMessageListenerContainer.removeMessageListener(messageListenerMap.remove(session));
  }

  private String genSubKey(WebSocketSession session) {
    return String.format("sub:%s:%s",
        SessionUtil.getUsernameFromSession(session), session.getId());
  }
}
