package com.jchat.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.socket.WebSocketSession;

public interface SessionService {

  /**
   * 增加 session
   */
  void add(WebSocketSession session) throws IOException;

  /**
   * 移除 session
   */
  void remove(WebSocketSession session) throws IOException;

  /**
   * 获取所有 session
   */
  List<WebSocketSession> getSessions();

  int getSessionCount();

}
