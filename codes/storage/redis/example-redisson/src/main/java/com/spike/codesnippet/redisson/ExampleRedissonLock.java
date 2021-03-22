package com.spike.codesnippet.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * 
 */
public class ExampleRedissonLock {

  public static void main(String[] args) {
    // 创建客户端
    Config config = new Config();
    config.useSingleServer().setAddress("redis://127.0.0.1:6379");
    final RedissonClient redissonClient = Redisson.create(config);

    final String lockName = "biz1:module1:lock";

    Worker worker1 = new Worker(redissonClient, lockName);
    Worker worker2 = new Worker(redissonClient, lockName);

    Thread thread1 = new Thread(worker1, "Worker1");
    Thread thread2 = new Thread(worker2, "Worker2");
    thread1.start();
    thread2.start();

    try {
      thread1.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    try {
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  static class Worker implements Runnable {

    private final String lockName;
    private final RedissonClient redissonClient;

    public Worker(RedissonClient redissonClient, String lockName) {
      this.redissonClient = redissonClient;
      this.lockName = lockName;
    }

    @Override
    public void run() {
      // 获取锁实例
      RLock lock = redissonClient.getLock(lockName);

      while (true) {
        try {
          System.err.println(Thread.currentThread().getName() + " try to got the lock!");
          lock.lock(); // 加锁
          System.err.println(Thread.currentThread().getName() + " got the lock!!!");
        } finally {
          lock.unlock(); // 解锁
          System.err.println(Thread.currentThread().getName() + " released the lock!");
        }
      }
    }

  }
}
