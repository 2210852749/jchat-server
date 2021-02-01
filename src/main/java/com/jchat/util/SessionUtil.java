package com.jchat.util;

import com.jchat.model.User;
import org.springframework.web.socket.WebSocketSession;

public class SessionUtil {

  public static final String USER = "user";

  public static User getUserFromSession(WebSocketSession session) {
    return (User) session.getAttributes().get(USER);
  }

  public static String getUsernameFromSession(WebSocketSession session) {
    return ((User) session.getAttributes().get(USER)).getUsername();
  }
}
