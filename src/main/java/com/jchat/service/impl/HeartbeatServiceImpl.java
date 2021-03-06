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

  private final ExecutorService[] executors;
  private final DelayQueue<HeartbeatSessionTask>[] bucket;

  private static final int BUCKET_SIZE = 10;

  @SuppressWarnings("unchecked")
  public HeartbeatServiceImpl() {
    this.bucket = new DelayQueue[BUCKET_SIZE];
    this.executors = new ExecutorService[BUCKET_SIZE];
    for (int i = 0; i < BUCKET_SIZE; i++) {
      bucket[i] = new DelayQueue<>();
      executors[i] = Executors.newSingleThreadExecutor();

      int index = i;

      executors[index].submit(() -> {
        while (true) {
          try {
            HeartbeatSessionTask task;
            while ((task = bucket[index].poll()) != null) {
              // 有可能已经关闭了，则直接跳过
              if (task.getSession().isOpen()) {
                task.getSession().close();
                log.warn("[{}] is dead, so close",
                    SessionUtil.getUsernameFromSession(task.session));
              }
            }
          } catch (Exception e) {
            log.error("", e);
          }
          Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        }
      });
    }
  }

  @Override
  public void add(WebSocketSession session) {
    bucket[getBucket(session)].add(new HeartbeatSessionTask(session));
  }

  @Override
  public void remove(WebSocketSession session) {
    bucket[getBucket(session)].removeIf(task -> task.getSession() == session);
  }

  @Override
  public void refresh(WebSocketSession session) {
    bucket[getBucket(session)].removeIf(task -> task.getSession() == session);
    bucket[getBucket(session)].add(new HeartbeatSessionTask(session));
  }

  private int getBucket(WebSocketSession session) {
    return Math.abs(session.getId().hashCode() % BUCKET_SIZE);
  }

  @PreDestroy
  public void onDestroy() throws InterruptedException {
    for (int i = 0; i < BUCKET_SIZE; i++) {
      executors[i].shutdown();
      executors[i].awaitTermination(5, TimeUnit.SECONDS);
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
