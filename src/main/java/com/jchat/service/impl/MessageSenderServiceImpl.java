package com.jchat.service.impl;

import com.jchat.service.MessageSenderService;
import com.jchat.service.SessionService;
import com.jchat.service.SubPubService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class MessageSenderServiceImpl implements MessageSenderService {

  private final SubPubService subPubService;
  private final SessionService sessionService;

  public MessageSenderServiceImpl(SubPubService subPubService, SessionService sessionService) {
    this.subPubService = subPubService;
    this.sessionService = sessionService;
  }

  @Override
  public void sendToAll(TextMessage textMessage) {
    sessionService.getSessions()
        .forEach(session -> subPubService.pub(session, textMessage));
  }
}
