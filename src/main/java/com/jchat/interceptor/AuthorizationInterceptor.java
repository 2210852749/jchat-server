package com.jchat.interceptor;

import static com.jchat.service.impl.SessionServiceImpl.USER;

import com.jchat.exception.AuthorizationException;
import com.jchat.model.User;
import com.jchat.service.UserService;
import java.io.OutputStream;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
@Component
public class AuthorizationInterceptor implements HandshakeInterceptor {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";

  private final UserService userService;

  public AuthorizationInterceptor(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) {
    try {
      String username = ((ServletServerHttpRequest) request)
          .getServletRequest().getParameter(USERNAME);
      String password = ((ServletServerHttpRequest) request)
          .getServletRequest().getParameter(PASSWORD);
      User user = userService.register(username, password);
      attributes.put(USER, user);
    } catch (AuthorizationException e) {
      log.error("", e);
      try (OutputStream outputStream = response.getBody()) {
        outputStream.write(e.getMessage().getBytes());
        outputStream.flush();
      } catch (Exception err) {
        log.error("", err);
      }
      response.close();
    }
    return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception exception) {
  }
}
