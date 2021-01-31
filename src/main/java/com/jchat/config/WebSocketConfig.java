package com.jchat.config;

import com.jchat.handler.WebSocketTextMessageHandler;
import com.jchat.interceptor.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

  private final AuthorizationInterceptor authorizationInterceptor;
  private final WebSocketTextMessageHandler webSocketTextMessageHandler;

  public WebSocketConfig(AuthorizationInterceptor authorizationInterceptor,
      WebSocketTextMessageHandler webSocketTextMessageHandler) {
    this.authorizationInterceptor = authorizationInterceptor;
    this.webSocketTextMessageHandler = webSocketTextMessageHandler;
  }

  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketTextMessageHandler, "/ws")
        .addInterceptors(authorizationInterceptor)
        .setAllowedOrigins("*");
  }
}
