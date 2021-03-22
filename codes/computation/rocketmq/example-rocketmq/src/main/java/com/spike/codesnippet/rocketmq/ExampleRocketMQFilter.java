package com.spike.codesnippet.rocketmq;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import com.google.common.collect.Lists;

/**
 * 
 */
public class ExampleRocketMQFilter {

  public static void main(String[] args) {
    final ExampleRocketMQFilter example = new ExampleRocketMQFilter();

    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.sendFilterMessage();
      }
    });
    t1.start();

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.consumeFilterMessage();
      }
    });
    t2.start();

    try {
      t1.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    try {
      t2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  public void sendFilterMessage() {

    DefaultMQProducer producer =
        new DefaultMQProducer(this.getClass().getSimpleName() + "_PRODUCER_GROUP");
    producer.setNamesrvAddr("127.0.0.1:9876");
    try {
      producer.start();

      for (int i = 0; i < 10; i++) {
        try {
          Message message = new Message(//
              this.getClass().getSimpleName() + "_Topic", // topic
              "TagA", // tags
              ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) // body
          );

          // 设置消息的用户属性
          message.putUserProperty("a", String.valueOf(i));

          SendResult sendResult = producer.send(message);
          System.out.println(sendResult.getMsgId() + ": " + sendResult);
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

  public void consumeFilterMessage() {

    // push/pull
    DefaultMQPushConsumer consumer =
        new DefaultMQPushConsumer(this.getClass().getSimpleName() + "_CONSUMER_GROUP");
    consumer.setNamesrvAddr("127.0.0.1:9876");

    try {
      // 订阅: 使用消息选择器
      MessageSelector messageSelector = MessageSelector.bySql("a between 0 and 3");
      consumer.subscribe(this.getClass().getSimpleName() + "_Topic", messageSelector);
    } catch (MQClientException e) {
      e.printStackTrace();
    }
    MessageListenerConcurrently messageListener = new MessageListenerConcurrently() {

      @Override
      public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
          ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
          System.err.println(Thread.currentThread().getName() + " revice pushed messages: "
              + msg.getMsgId() + ", " + msg.getUserProperty("a") + ", " + msg);
        }
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
