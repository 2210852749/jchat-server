package com.jchat.service;

import com.jchat.model.User;
import java.util.List;
import org.springframework.web.socket.WebSocketSession;

public interface SessionService {

  /**
   * 增加 session
   */
  void add(WebSocketSession session);

  /**
   * 移除 session
   */
  void remove(WebSocketSession session);

  /**
   * 从 session 里面获取用户
   */
  User getUserFromSession(WebSocketSession session);

  /**
   * 获取所有 session
   */
  List<WebSocketSession> getSessions();

  int getSessionCount();

}
