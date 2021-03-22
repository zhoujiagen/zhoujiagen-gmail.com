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
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 
 */
public class ExampleRocketMQSimple {
  public static void main(String[] args) {
    final ExampleRocketMQSimple example = new ExampleRocketMQSimple();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        // example.sendMessageSynchronously();
        example.sendMessageAsynchronously();
      }
    });
    t1.start();

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        example.consumeMessage();
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

  public void sendMessageSynchronously() {
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

  public void sendMessageAsynchronously() {
    DefaultMQProducer producer =
        new DefaultMQProducer(this.getClass().getSimpleName() + "_PRODUCER_GROUP");
    producer.setNamesrvAddr("127.0.0.1:9876");

    try {
      producer.start();
      producer.setRetryTimesWhenSendAsyncFailed(0); // 不重试

      for (int i = 0; i < 100; i++) {
        final int index = 1;
        try {
          Message message = new Message(//
              "TestTopic", // topic
              "TagA", // tags,
              "OrderID188", // keys
              ("Hello Order " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) // body
          );

          producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
              System.out.println(index + " :" + sendResult);
            }

            @Override
            public void onException(Throwable e) {
              System.out.println(index + " : failed");
              e.printStackTrace();
            }
          });

        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        } catch (RemotingException e) {
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

  public void consumeMessage() {
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
