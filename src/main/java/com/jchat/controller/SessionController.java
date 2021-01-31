package com.jchat.controller;

import com.jchat.model.User;
import com.jchat.response.PageResponse;
import com.jchat.service.SessionService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

  private final SessionService sessionService;

  public SessionController(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @GetMapping("/page")
  public PageResponse<String> page(
      @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
      @RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {
    List<String> usernames = sessionService.getSessions()
        .stream()
        .map(sessionService::getUserFromSession)
        .map(User::getUsername)
        .skip(offset)
        .limit(limit)
        .collect(Collectors.toList());
    return PageResponse.success(usernames, sessionService.getSessionCount());
  }
}
