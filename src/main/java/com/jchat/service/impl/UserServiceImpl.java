package com.jchat.service.impl;

import com.jchat.exception.AuthorizationException;
import com.jchat.model.User;
import com.jchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @Override
  public User register(String username, String password) throws AuthorizationException {
    // 如果 key 不存在，则认为用户未注册，那么直接注册并返回
    Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(genUserKey(username), password);
    if (result == Boolean.TRUE) {
      return new User(username, password);
    }
    // 如果进入，则代表已存在，则判断下密码是否正确，如果正确也返回
    String existPassword = stringRedisTemplate.opsForValue().get(genUserKey(username));
    if (password.equals(existPassword)) {
      return new User(username, password);
    }
    throw new AuthorizationException("username password not paired");
  }

  private String genUserKey(String username) {
    return String.format("username:%s", username);
  }
}
