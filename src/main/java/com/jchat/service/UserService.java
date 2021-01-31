package com.jchat.service;

import com.jchat.exception.AuthorizationException;
import com.jchat.model.User;

public interface UserService {

  /**
   * @param username 用户帐号
   * @param password 用户密码
   * @return 如果 username 不存在，那么会返回注册好的 username / password 如果 username 已存在，password 也匹配，那么也会返回
   * username / password 否则，返回 null
   */
  User register(String username, String password) throws AuthorizationException;

}
