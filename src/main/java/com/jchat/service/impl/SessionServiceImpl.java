package com.jchat.service.impl;

import com.jchat.service.SessionService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

  private final List<WebSocketSession> sessions;

  @Autowired
  private SessionServiceImpl() {
    sessions = new CopyOnWriteArrayList<>();
  }

  @Override
  public void add(WebSocketSession session) {
    if (session != null) {
      sessions.add(session);
    }
  }

  @Override
  public void remove(WebSocketSession session) {
    if (session != null) {
      sessions.remove(session);
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
}
