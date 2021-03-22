package com.spike.codesnippet.netty.example.eventloop;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务调度示例
 * @author zhoujiagen
 * @see java.util.concurrent.ScheduledExecutorService
 * @see java.util.concurrent.Executors
 * @see java.util.concurrent.ScheduledFuture<?>
 * @see io.netty.channel.EventLoop
 * @see io.netty.util.concurrent.ScheduledFuture<?>
 */
public class TaskScheduling {
  private static final Logger LOG = LoggerFactory.getLogger(TaskScheduling.class);

  public static void main(String[] args) {
    // javanative();

    // eventloop_delay();
    eventloop_fixedrate();
  }

  static void eventloop_fixedrate() {
    java.util.concurrent.ExecutorService executor = Executors.newFixedThreadPool(2);
    EventLoop eventLoop = new DefaultEventLoop(executor);

    io.netty.util.concurrent.ScheduledFuture<?> future =
        eventLoop.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);

    try {
      Thread.sleep(4000L);
      future.cancel(false); // cancel
      Thread.sleep(4000L);
      eventLoop.shutdownGracefully().get();
    } catch (InterruptedException | ExecutionException e) {
      LOG.error("", e);
    } finally {
      executor.shutdown();
    }
  }

  static void eventloop_delay() {
    java.util.concurrent.ExecutorService executor = Executors.newFixedThreadPool(2);
    EventLoop eventLoop = new DefaultEventLoop(executor);

    try {
      eventLoop.schedule(task, 5, TimeUnit.SECONDS).get();
      eventLoop.shutdownGracefully().get();
    } catch (InterruptedException | ExecutionException e) {
      LOG.error("", e);
    } finally {
      executor.shutdown();
    }
  }

  static void javanative() {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    ScheduledFuture<?> future = executorService.schedule(task, 5, TimeUnit.SECONDS);
    try {
      future.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    } finally {
      executorService.shutdown();
    }
  }

  static Runnable task = new Runnable() {
    @Override
    public void run() {
      LOG.info("Well, I'm doing something.");
    }
  };
}
