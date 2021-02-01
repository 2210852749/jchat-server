package com.jchat.listener;

import com.jchat.event.SessionCloseEvent;
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
public class SessionCloseEventListener implements ApplicationListener<SessionCloseEvent> {

  private final SubPubService subPubService;
  private final SessionService sessionService;
  private final HeartbeatService heartbeatService;
  private final MessageSenderService messageSenderService;

  public SessionCloseEventListener(SubPubService subPubService,
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
  public void onApplicationEvent(SessionCloseEvent event) {
    sessionService.remove(event.getSession());
    subPubService.unSub(event.getSession());
    heartbeatService.remove(event.getSession());

    String message = String.format("[%s] 下线啦",
        SessionUtil.getUsernameFromSession(event.getSession()));
    messageSenderService.sendToAll(new TextMessage(message));
  }
}
