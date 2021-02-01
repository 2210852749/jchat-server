package com.jchat.service.impl;

import com.jchat.service.SessionService;
import com.jchat.service.SubPubService;
import com.jchat.util.SessionUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

  private final SubPubService subPubService;
  private final List<WebSocketSession> sessions;
  private final StringRedisTemplate stringRedisTemplate;

  @Autowired
  private SessionServiceImpl(SubPubService subPubService,
      StringRedisTemplate stringRedisTemplate) {
    this.subPubService = subPubService;
    this.stringRedisTemplate = stringRedisTemplate;
    sessions = new CopyOnWriteArrayList<>();
  }

  @Override
  public void add(WebSocketSession session) throws IOException {
    if (session != null) {
      String username = SessionUtil.getUsernameFromSession(session);
      String sessionKey = genSessionKey(username);
      if (stringRedisTemplate.opsForValue().setIfAbsent(sessionKey, "1") == Boolean.TRUE) {
        log.info("[{}] online, welcome", username);
        sessions.add(session);
        subPubService.sub(session);
      } else {
        log.warn("[{}] really online, can not online again", username);
        session.sendMessage(new TextMessage("[System]: you really online, so exit"));
        session.close(CloseStatus.SERVICE_OVERLOAD);
      }
    }
  }

  @Override
  public void remove(WebSocketSession session) {
    if (session != null) {
      String sessionKey = genSessionKey(SessionUtil.getUsernameFromSession(session));
      if (stringRedisTemplate.delete(sessionKey) == Boolean.TRUE) {
        sessions.remove(session);
        subPubService.unSub(session);
      }
    }
  }

  @Override
  public List<WebSocketSession> getSessions() {
    return new ArrayList<>(sessions);
  }

  @Override
  public int getSessionCount() {
    return sessions.size();
  }

  private String genSessionKey(String username) {
    return String.format("session:%s", username);
  }
}
