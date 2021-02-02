package com.jchat.service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface PubSubService {

  void pub(WebSocketSession session, TextMessage textMessage);

  void unSub(WebSocketSession session);

  void sub(WebSocketSession session);

}
