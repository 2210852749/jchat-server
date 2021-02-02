package com.jchat.service.impl;

import com.jchat.service.MessageSenderService;
import com.jchat.service.PubSubService;
import com.jchat.service.SessionService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class MessageSenderServiceImpl implements MessageSenderService {

  private final PubSubService pubSubService;
  private final SessionService sessionService;

  public MessageSenderServiceImpl(PubSubService pubSubService,
      SessionService sessionService) {
    this.pubSubService = pubSubService;
    this.sessionService = sessionService;
  }

  @Override
  public void sendToAll(TextMessage textMessage) {
    sessionService.getSessions()
        .forEach(session -> pubSubService.pub(session, textMessage));
  }
}
