package com.jchat.service;

import org.springframework.web.socket.TextMessage;

public interface MessageSenderService {

  void sendToAll(TextMessage textMessage);

}
