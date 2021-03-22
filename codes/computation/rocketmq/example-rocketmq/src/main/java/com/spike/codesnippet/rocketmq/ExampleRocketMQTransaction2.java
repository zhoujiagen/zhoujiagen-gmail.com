package com.spike.codesnippet.rocketmq;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import com.google.common.collect.Maps;

/**
 * Mock another producer with
 */
public class ExampleRocketMQTransaction2 {

  public static void main(String[] args) {
    final ExampleRocketMQTransaction2 example = new ExampleRocketMQTransaction2();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.sendTransactionalMessage();
      }
    });
    t1.start();

    try {
      t1.join();
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
    System.out.println("sendTransactionalMessage done!");

    try {
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendTransactionalMessage() {

    // 相同的生产者组
    TransactionMQProducer producer = new TransactionMQProducer(
        ExampleRocketMQTransaction.class.getSimpleName() + "_PRODUCER_GROUP");
    producer.setNamesrvAddr("172.168.1.185:9876");
    ExecutorService executorService = new ThreadPoolExecutor(//
        2, // corePoolSize,
        5, // maximumPoolSize,
        100, // keepAliveTime,
        TimeUnit.SECONDS, // unit,
        new ArrayBlockingQueue<Runnable>(2000), // workQueue,
        new ThreadFactory() {
          @Override
          public Thread newThread(Runnable r) {
            Thread thread = new Thread();
            thread.setName(this.getClass().getSimpleName() + "Worker");
            return thread;
          }
        }, // threadFactory,
        new ThreadPoolExecutor.CallerRunsPolicy() // handler
    );
    producer.setExecutorService(executorService);
    // 事务监听器
    TransactionListener transactionListener = new TransactionListenerStub();
    producer.setTransactionListener(transactionListener);
    producer.setClientIP("172.168.1.185");
    // producer.setUseTLS(true);

    try {
      producer.start();
      System.out.println(
        "producer ip=" + producer.getClientIP() + ", instanceName=" + producer.getInstanceName());

      for (int i = 0; i < 100000; i++) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

    } catch (MQClientException e) {
      e.printStackTrace();
    } finally {
      producer.shutdown();
    }

  }

  static class TransactionListenerStub implements TransactionListener {

    private final AtomicLong transactionCount = new AtomicLong(0);
    private final ConcurrentMap<String, Integer> localTransactionStatusStore =
        Maps.<String, Integer> newConcurrentMap();

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
      // 执行本地事务
      transactionCount.incrementAndGet();
      int status = (int) (transactionCount.get() % 3);
      System.err.println(Thread.currentThread().getName() + " executeLocalTransaction: msg=" + msg
          + ", arg=" + arg + ", status=" + status);
      localTransactionStatusStore.put(msg.getTransactionId(), status);
      System.err.println("localTransactionStatusStore=" + localTransactionStatusStore);
      return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
      // 检查本地事务: 总是返回提交
      System.err.println(Thread.currentThread().getName() + " checkLocalTransaction: msg=" + msg
          + ", state=COMMIT_MESSAGE");
      return LocalTransactionState.COMMIT_MESSAGE;
    }
  }

}
