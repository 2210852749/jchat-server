package com.jchat.listener;

import com.jchat.event.PongMessageEvent;
import com.jchat.service.HeartbeatService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PongMessageEventListener implements ApplicationListener<PongMessageEvent> {

  private final HeartbeatService heartbeatService;

  public PongMessageEventListener(HeartbeatService heartbeatService) {
    this.heartbeatService = heartbeatService;
  }

  @Override
  public void onApplicationEvent(PongMessageEvent event) {
    heartbeatService.refresh(event.getSession());
  }
}
