package com.jchat.event;

import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class TextMessageEvent extends BaseSessionEvent {

  private final TextMessage textMessage;

  public TextMessageEvent(Object source, WebSocketSession session, TextMessage textMessage) {
    super(source, session);
    this.textMessage = textMessage;
  }
}
