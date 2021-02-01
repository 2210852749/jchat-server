package com.jchat.handler;

import com.jchat.service.MessageService;
import com.jchat.service.SessionService;
import com.jchat.util.SessionUtil;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class WebSocketTextMessageHandler extends TextWebSocketHandler {

  private final SessionService sessionService;
  private final MessageService messageService;

  @Autowired
  public WebSocketTextMessageHandler(SessionService sessionService,
      MessageService messageService) {
    this.sessionService = sessionService;
    this.messageService = messageService;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    sessionService.add(session);
    String message = String.format("[%s] 上线啦",
        SessionUtil.getUsernameFromSession(session));
    messageService.sendMessageToAll(new TextMessage(message));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
      throws IOException {
    if (status.getCode() == CloseStatus.SERVICE_OVERLOAD.getCode()) {
      sessionService.remove(session);
      String message = String.format("[%s] 下线啦",
          SessionUtil.getUsernameFromSession(session));
      messageService.sendMessageToAll(new TextMessage(message));
    }
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    messageService.handle(session, message);
  }
}
