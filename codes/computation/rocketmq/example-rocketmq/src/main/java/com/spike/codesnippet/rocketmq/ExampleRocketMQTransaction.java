package com.spike.codesnippet.rocketmq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import com.google.common.collect.Maps;

/**
 * <pre>
 * FIXME(zhoujiagen) 没有回调checkLocalTransaction.
 * 
 * CODE:206 DESC:the consumer group[CID_RMQ_SYS_TRANS] not online
 * </pre>
 */
public class ExampleRocketMQTransaction {

  public static void main(String[] args) {
    final ExampleRocketMQTransaction example = new ExampleRocketMQTransaction();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.sendTransactionalMessage();
      }
    });
    t1.start();
    //
    // Thread t2 = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // example.consumeTransactionalMessage();
    // }
    // });
    // t2.start();

    try {
      t1.join();
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
    System.out.println("sendTransactionalMessage done!");
    // try {
    // t2.join();
    // } catch (InterruptedException e1) {
    // e1.printStackTrace();
    // }

    try {
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendTransactionalMessage() {

    TransactionMQProducer producer =
        new TransactionMQProducer(this.getClass().getSimpleName() + "_PRODUCER_GROUP");
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

      for (int i = 0; i < 10; i++) {
        try {
          Message message = new Message(//
              this.getClass().getSimpleName() + "_Topic", // topic
              "TagA", // tags
              "KEY_" + new Date().getTime() + "_" + i, // keys
              ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) // body
          );

          // 发送事务消息
          TransactionSendResult sendResult = producer.sendMessageInTransaction(message, null);
          System.out.println(sendResult.getMsgId() + ": " + sendResult);

          try {
            Thread.sleep(200L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }

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
      // 检查本地事务
      Integer status = localTransactionStatusStore.get(msg.getTransactionId());
      if (status != null) {
        switch (status) {
        case 0:
          System.err.println(Thread.currentThread().getName() + " checkLocalTransaction: msg=" + msg
              + ", state=UNKNOW");
          return LocalTransactionState.UNKNOW;
        case 1:
          System.err.println(Thread.currentThread().getName() + " checkLocalTransaction: msg=" + msg
              + ", state=COMMIT_MESSAGE");
          return LocalTransactionState.COMMIT_MESSAGE;
        case 2:
          System.err.println(Thread.currentThread().getName() + " checkLocalTransaction: msg=" + msg
              + ", state=ROLLBACK_MESSAGE");
          return LocalTransactionState.ROLLBACK_MESSAGE;
        default:
          System.err.println(Thread.currentThread().getName() + " checkLocalTransaction: msg=" + msg
              + ", state=UNKNOW");
          return LocalTransactionState.UNKNOW;
        }
      }

      System.err.println(Thread.currentThread().getName() + " checkLocalTransaction: msg=" + msg
          + ", state=COMMIT_MESSAGE");
      return LocalTransactionState.COMMIT_MESSAGE;
    }
  }

  public void consumeTransactionalMessage() {
    // push/pull
    DefaultMQPushConsumer consumer =
        new DefaultMQPushConsumer(this.getClass().getSimpleName() + "_CONSUMER_GROUP");
    consumer.setNamesrvAddr("127.0.0.1:9876");

    try {
      // 订阅
      consumer.subscribe(this.getClass().getSimpleName() + "_Topic", "*");
    } catch (MQClientException e) {
      e.printStackTrace();
    }
    MessageListenerConcurrently messageListener = new MessageListenerConcurrently() {

      @Override
      public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
          ConsumeConcurrentlyContext context) {
        System.err.println(Thread.currentThread().getName() + " revice pushed messages: " + msgs);
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
      }

    };
    consumer.registerMessageListener(messageListener);

    try {
      consumer.start();
    } catch (MQClientException e) {
      e.printStackTrace();
    }
  }

}
