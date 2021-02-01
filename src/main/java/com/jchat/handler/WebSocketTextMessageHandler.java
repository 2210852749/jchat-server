package com.jchat.handler;

import com.jchat.event.PongMessageEvent;
import com.jchat.event.SessionCloseEvent;
import com.jchat.event.SessionEstablishedEvent;
import com.jchat.event.TextMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class WebSocketTextMessageHandler extends TextWebSocketHandler {

  private final ApplicationEventPublisher publisher;

  @Autowired
  public WebSocketTextMessageHandler(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    publisher.publishEvent(new SessionEstablishedEvent(this, session));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    publisher.publishEvent(new SessionCloseEvent(this, session, status));
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    publisher.publishEvent(new TextMessageEvent(this, session, message));
  }

  @Override
  protected void handlePongMessage(WebSocketSession session, PongMessage message) {
    publisher.publishEvent(new PongMessageEvent(this, session, message));
  }
}
