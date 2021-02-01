package com.jchat.service.impl;

import com.jchat.service.HeartbeatService;
import com.jchat.util.SessionUtil;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class HeartbeatServiceImpl implements HeartbeatService {

  private final ExecutorService executorService;
  private final DelayQueue<HeartbeatSessionTask> queue;

  public HeartbeatServiceImpl() {
    this.queue = new DelayQueue<>();
    this.executorService = Executors.newSingleThreadExecutor();
    this.executorService.submit(() -> {
      while (true) {
        try {
          HeartbeatSessionTask task;
          while ((task = queue.poll()) != null) {
            task.getSession().close();
            log.warn("[{}] is dead, so close", SessionUtil.getUsernameFromSession(task.session));
          }
        } catch (Exception e) {
          log.error("", e);
        }
        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
      }
    });
  }

  @Override
  public void add(WebSocketSession session) {
    queue.add(new HeartbeatSessionTask(session));
  }

  @Override
  public void remove(WebSocketSession session) {
    queue.removeIf(task -> task.getSession() == session);
  }

  @Override
  public void refresh(WebSocketSession session) {
    queue.removeIf(task -> task.getSession() == session);
    queue.add(new HeartbeatSessionTask(session));
  }

  @PreDestroy
  public void onDestroy() throws InterruptedException {
    if (executorService != null) {
      executorService.shutdown();
      executorService.awaitTermination(5, TimeUnit.SECONDS);
    }
  }

  @Getter
  private static class HeartbeatSessionTask implements Delayed {

    private final long timeout;
    private final WebSocketSession session;

    private HeartbeatSessionTask(WebSocketSession session) {
      this.session = session;
      this.timeout = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);
    }

    @Override
    public long getDelay(TimeUnit unit) {
      return timeout - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
      HeartbeatSessionTask otherTask = (HeartbeatSessionTask) o;
      return NumberUtils.compare(timeout, otherTask.timeout);
    }
  }
}
