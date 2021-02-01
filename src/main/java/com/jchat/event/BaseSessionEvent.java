package com.jchat.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class BaseSessionEvent extends ApplicationEvent {

  protected final WebSocketSession session;

  public BaseSessionEvent(Object source, WebSocketSession session) {
    super(source);
    this.session = session;
  }
}
