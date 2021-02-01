package com.jchat.event;

import lombok.Getter;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class PongMessageEvent extends BaseSessionEvent {

  private final PongMessage pongMessage;

  public PongMessageEvent(Object source, WebSocketSession session, PongMessage pongMessage) {
    super(source, session);
    this.pongMessage = pongMessage;
  }
}
