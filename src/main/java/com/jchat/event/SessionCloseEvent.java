package com.jchat.event;

import lombok.Getter;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class SessionCloseEvent extends BaseSessionEvent {

  private final CloseStatus closeStatus;

  public SessionCloseEvent(Object source, WebSocketSession session, CloseStatus closeStatus) {
    super(source, session);
    this.closeStatus = closeStatus;
  }
}
