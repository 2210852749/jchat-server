package com.jchat.service;

import java.io.IOException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface MessageService {

  void handle(WebSocketSession from, TextMessage message) throws IOException;

  void sendMessageToAll(TextMessage message) throws IOException;

}
