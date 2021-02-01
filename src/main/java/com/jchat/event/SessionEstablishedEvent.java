package com.jchat.event;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class SessionEstablishedEvent extends BaseSessionEvent {

  public SessionEstablishedEvent(Object source, WebSocketSession session) {
    super(source, session);
  }
}
