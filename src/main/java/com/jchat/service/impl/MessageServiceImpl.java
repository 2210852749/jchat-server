package com.jchat.service.impl;

import com.jchat.model.User;
import com.jchat.service.MessageService;
import com.jchat.service.SessionService;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class MessageServiceImpl implements MessageService {

  private final SessionService sessionService;

  public MessageServiceImpl(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Override
  public void handle(WebSocketSession from, TextMessage message) throws IOException {
    String payload = message.getPayload();
    User user = sessionService.getUserFromSession(from);
    TextMessage textMessage =
        new TextMessage(String.format("[%s]: %s", user.getUsername(), payload));
    this.sendMessageToAll(textMessage);
  }

  @Override
  public void sendMessageToAll(TextMessage message) throws IOException {
    List<WebSocketSession> sessions = sessionService.getSessions();
    for (WebSocketSession targetSession : sessions) {
      targetSession.sendMessage(message);
    }
  }
}
