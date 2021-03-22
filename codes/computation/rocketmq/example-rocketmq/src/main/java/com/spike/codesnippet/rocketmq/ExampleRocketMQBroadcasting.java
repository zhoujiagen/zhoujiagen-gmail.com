package com.spike.codesnippet.rocketmq;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 
 */
public class ExampleRocketMQBroadcasting {

  public static void main(String[] args) {
    final ExampleRocketMQBroadcasting example = new ExampleRocketMQBroadcasting();

    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.consumeBroadcasetMessage();
      }
    });
    t1.start();
    // 同一个JVM内只生成生产者/消费者组中的一个生产者/消费者实例
    // Thread t2 = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // example.consumeBroadcasetMessage();
    // }
    // });
    // t2.start();

    try {
      Thread.sleep(5000L);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

    Thread t3 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.sendBroadcasetMessage();
      }
    });
    t3.start();

    try {
      t1.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    // try {
    // t2.join();
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    try {
      t3.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void sendBroadcasetMessage() {
    DefaultMQProducer producer =
        new DefaultMQProducer(this.getClass().getSimpleName() + "_PRODUCER_GROUP");
    producer.setNamesrvAddr("127.0.0.1:9876");
    try {
      producer.start();

      for (int i = 0; i < 100; i++) {
        try {
          Message message = new Message(//
              this.getClass().getSimpleName() + "_Topic", // topic
              "TagA", // tags
              "OrderID188", // keys
              ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) // body
          );

          SendResult sendResult = producer.send(message);
          System.out.println(sendResult);

        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        } catch (RemotingException e) {
          e.printStackTrace();
        } catch (MQBrokerException e) {
          e.printStackTrace();
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

  public void consumeBroadcasetMessage() {
    DefaultMQPushConsumer consumer =
        new DefaultMQPushConsumer(this.getClass().getSimpleName() + "_CONSUMER_GROUP");
    consumer.setNamesrvAddr("127.0.0.1:9876");

    consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    // 设置为广播模式, 默认为CLUSTERING
    consumer.setMessageModel(MessageModel.BROADCASTING);

    try {
      consumer.subscribe(//
        this.getClass().getSimpleName() + "_Topic", // topic
        "TagA || TagC || TagD" // messageSelector
      );

      MessageListenerConcurrently messageListener = new MessageListenerConcurrently() {
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
            ConsumeConcurrentlyContext context) {
          System.err.println(Thread.currentThread().getName() + " receved: " + msgs);
          return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
      };
      consumer.registerMessageListener(messageListener);

      consumer.start();
    } catch (MQClientException e) {
      e.printStackTrace();
    }
  }
}
