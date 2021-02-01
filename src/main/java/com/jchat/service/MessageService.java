package com.jchat.service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface MessageService {

  void handle(WebSocketSession from, TextMessage message);

  void sendMessageToAll(TextMessage message);

}
