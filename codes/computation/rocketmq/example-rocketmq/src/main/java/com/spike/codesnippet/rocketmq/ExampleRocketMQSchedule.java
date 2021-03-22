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

/**
 * 
 */
public class ExampleRocketMQSchedule {

  public static void main(String[] args) {

    final ExampleRocketMQSchedule example = new ExampleRocketMQSchedule();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.consumeScheduledMessage();
      }
    });
    t1.start();

    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.sendScheduledMessage();
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

  public void sendScheduledMessage() {
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
              ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) // body
          );

          // 设置消息延迟等级, 见配置项messageDelayLevel
          message.setDelayTimeLevel(3);

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

  public void consumeScheduledMessage() {
    DefaultMQPushConsumer consumer =
        new DefaultMQPushConsumer(this.getClass().getSimpleName() + "_CONSUMER_GROUP");
    consumer.setNamesrvAddr("127.0.0.1:9876");

    try {
      consumer.subscribe(this.getClass().getSimpleName() + "_Topic", "*");

      consumer.registerMessageListener(new MessageListenerConcurrently() {

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
            ConsumeConcurrentlyContext context) {
          for (MessageExt msg : msgs) {
            System.err.println(Thread.currentThread().getName() + " received: " + msg);
          }
          return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
      });

      consumer.start();
    } catch (MQClientException e) {
      e.printStackTrace();
    }

  }

}
