package com.jchat.service.impl;

import com.jchat.model.User;
import com.jchat.service.SessionService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class SessionServiceImpl implements SessionService {

  public static final String USER = "user";

  private static final List<WebSocketSession> SESSIONS = new CopyOnWriteArrayList<>();

  @Override
  public synchronized void add(WebSocketSession session) {
    if (session != null) {
      SESSIONS.add(session);
    }
  }

  @Override
  public synchronized void remove(WebSocketSession session) {
    if (session != null) {
      SESSIONS.remove(session);
    }
  }

  @Override
  public User getUserFromSession(WebSocketSession session) {
    return (User) session.getAttributes().get(USER);
  }

  @Override
  public List<WebSocketSession> getSessions() {
    return new ArrayList<>(SESSIONS);
  }

  @Override
  public int getSessionCount() {
    return SESSIONS.size();
  }
}
