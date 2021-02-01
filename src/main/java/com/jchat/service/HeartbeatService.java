package com.jchat.service;

import org.springframework.web.socket.WebSocketSession;

public interface HeartbeatService {

  void add(WebSocketSession session);

  void remove(WebSocketSession session);

  void refresh(WebSocketSession session);

}
