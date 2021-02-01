package com.jchat.service.impl;

import com.jchat.service.MessageService;
import com.jchat.service.SessionService;
import com.jchat.service.SubPubService;
import com.jchat.util.SessionUtil;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class MessageServiceImpl implements MessageService {

  private final SubPubService subPubService;
  private final SessionService sessionService;

  public MessageServiceImpl(SubPubService subPubService,
      SessionService sessionService) {
    this.subPubService = subPubService;
    this.sessionService = sessionService;
  }

  @Override
  public void handle(WebSocketSession from, TextMessage message) {
    String payload = message.getPayload();
    TextMessage textMessage =
        new TextMessage(String.format("[%s]: %s",
            SessionUtil.getUsernameFromSession(from), payload));
    sendMessageToAll(textMessage);
  }

  @Override
  public void sendMessageToAll(TextMessage message) {
    List<WebSocketSession> sessions = sessionService.getSessions();
    for (WebSocketSession targetSession : sessions) {
      subPubService.pub(targetSession, message);
    }
  }
}
