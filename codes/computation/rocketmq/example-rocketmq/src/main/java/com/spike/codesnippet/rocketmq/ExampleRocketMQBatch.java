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
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import com.google.common.collect.Lists;

/**
 * 
 */
public class ExampleRocketMQBatch {

  public static void main(String[] args) {
    final ExampleRocketMQBatch example = new ExampleRocketMQBatch();

    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.sendBatchMessage();
      }
    });
    t1.start();

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.consumeBatchMessage();
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

  public void sendBatchMessage() {

    DefaultMQProducer producer =
        new DefaultMQProducer(this.getClass().getSimpleName() + "_PRODUCER_GROUP");
    producer.setNamesrvAddr("127.0.0.1:9876");
    try {
      producer.start();

      // limitation: 1MB
      List<Message> messages = Lists.newArrayList();
      for (int i = 0; i < 100; i++) {
        try {
          Message message = new Message(//
              this.getClass().getSimpleName() + "_Topic", // topic
              "TagA", // tags
              ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) // body
          );
          messages.add(message);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }

      try {
        SendResult sendResult = producer.send(messages);
        System.out.println(sendResult);
      } catch (RemotingException e) {
        e.printStackTrace();
      } catch (MQBrokerException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    } catch (MQClientException e) {
      e.printStackTrace();
    } finally {
      producer.shutdown();
    }

  }

  public void consumeBatchMessage() {

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
