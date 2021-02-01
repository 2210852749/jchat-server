package com.jchat.listener;

import com.jchat.event.SessionEstablishedEvent;
import com.jchat.service.HeartbeatService;
import com.jchat.service.MessageSenderService;
import com.jchat.service.SessionService;
import com.jchat.service.SubPubService;
import com.jchat.util.SessionUtil;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class SessionEstablishedEventListener
    implements ApplicationListener<SessionEstablishedEvent> {

  private final SubPubService subPubService;
  private final SessionService sessionService;
  private final HeartbeatService heartbeatService;
  private final MessageSenderService messageSenderService;

  public SessionEstablishedEventListener(SubPubService subPubService,
      SessionService sessionService,
      HeartbeatService heartbeatService,
      MessageSenderService messageSenderService) {
    this.subPubService = subPubService;
    this.sessionService = sessionService;
    this.heartbeatService = heartbeatService;
    this.messageSenderService = messageSenderService;
  }

  @Override
  @SneakyThrows
  public void onApplicationEvent(SessionEstablishedEvent event) {
    sessionService.add(event.getSession());
    subPubService.sub(event.getSession());
    heartbeatService.add(event.getSession());

    String message = String.format("[%s] 上线啦",
        SessionUtil.getUsernameFromSession(event.getSession()));
    messageSenderService.sendToAll(new TextMessage(message));
  }
}
