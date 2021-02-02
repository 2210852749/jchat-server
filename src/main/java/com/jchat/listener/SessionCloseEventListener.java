package com.jchat.listener;

import com.jchat.event.SessionCloseEvent;
import com.jchat.service.HeartbeatService;
import com.jchat.service.MessageSenderService;
import com.jchat.service.PubSubService;
import com.jchat.service.SessionService;
import com.jchat.util.SessionUtil;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class SessionCloseEventListener implements ApplicationListener<SessionCloseEvent> {

  private final PubSubService pubSubService;
  private final SessionService sessionService;
  private final HeartbeatService heartbeatService;
  private final MessageSenderService messageSenderService;

  public SessionCloseEventListener(PubSubService pubSubService,
      SessionService sessionService,
      HeartbeatService heartbeatService,
      MessageSenderService messageSenderService) {
    this.pubSubService = pubSubService;
    this.sessionService = sessionService;
    this.heartbeatService = heartbeatService;
    this.messageSenderService = messageSenderService;
  }

  @Override
  @SneakyThrows
  public void onApplicationEvent(SessionCloseEvent event) {
    sessionService.remove(event.getSession());
    pubSubService.unSub(event.getSession());
    heartbeatService.remove(event.getSession());

    String message = String.format("[%s] 下线啦",
        SessionUtil.getUsernameFromSession(event.getSession()));
    messageSenderService.sendToAll(new TextMessage(message));
  }
}
