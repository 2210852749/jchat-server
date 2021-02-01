package com.jchat.listener;

import com.jchat.event.TextMessageEvent;
import com.jchat.service.MessageSenderService;
import com.jchat.util.SessionUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class TextMessageEventListener implements ApplicationListener<TextMessageEvent> {

  private final MessageSenderService messageSenderService;

  public TextMessageEventListener(MessageSenderService messageSenderService) {
    this.messageSenderService = messageSenderService;
  }

  @Override
  public void onApplicationEvent(TextMessageEvent event) {
    String payload = event.getTextMessage().getPayload();

    TextMessage textMessage =
        new TextMessage(String.format("[%s]: %s",
            SessionUtil.getUsernameFromSession(event.getSession()), payload));
    messageSenderService.sendToAll(textMessage);
  }
}
