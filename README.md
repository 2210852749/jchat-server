## 一个简单、基于 springboot 分布式、支持同一帐号多设备登录 websocket 的 demo

#### 依赖
- maven
- jdk11
- redis

#### redis 配置
redis 默认使用 localhost:6379。如果需要修改 host:port，可以修改 application.yml
<br/>
redis 仅仅用于存储用户 username / password

#### 源码分析
###### [Auth 过程](https://github.com/yemingfeng/jchat-server/blob/master/src/main/java/com/jchat/interceptor/AuthorizationInterceptor.java)
```java
String username = ((ServletServerHttpRequest) request)
  .getServletRequest().getParameter(USERNAME);
String password = ((ServletServerHttpRequest) request)
  .getServletRequest().getParameter(PASSWORD);
User user = userService.register(username, password);
// 将 user 设置到 attributes 中
attributes.put(USER, user);
return true;
```

###### [连接创建](https://github.com/yemingfeng/jchat-server/blob/master/src/main/java/com/jchat/listener/SessionEstablishedEventListener.java)
```java
// 1. 添加 session
sessionService.add(session);
// 2. 按 username + sessionId 生成 redis key，并进行订阅，这样做可以支持多设备同一个帐号登录
MessageListener messageListener = (message, pattern) -> {
  log.info("Redis sub receive: [{}]", new String(message.getBody()));
  try {
    session.sendMessage(new TextMessage(message.getBody()));
  } catch (IOException e) {
    log.error("", e);
  }
};
redisMessageListenerContainer.addMessageListener(messageListener,
  new ChannelTopic(genSubKey(session)));
// 由于 session 会断开，需要保存下来，以待 removeListener
messageListenerMap.put(session, messageListener);
```

##### [监听消息](https://github.com/yemingfeng/jchat-server/blob/master/src/main/java/com/jchat/listener/TextMessageEventListener.java)
```java
// 获取所有在线的 session，然后通过 redis pub 功能转发消息
sessionService.getSessions()
  .forEach(session -> subPubService.pub(session, textMessage));
```

#### 使用
###### 服务端启动
启动后，会监听 localhost:8080 端口
<br/>
其中，websocket url 为 ws:localhost:8080/ws
<br/>
获取在线用户数接口为 http://localhost:8080/session/page

###### 前端测试
可以使用 http://coolaf.com/tool/chattest
<br/>
由于有简单的帐号体系，链接时需要制定 username / password，若 username 不存在，则直接注册成功；否则会判断 username / password 是否匹配
<br/>
如 ws://localhost:8080/ws?username=aiden&password=123 才能进行连接
图示：
![](./image1.png)
<br/>
<br/>
![](./image2.png)
