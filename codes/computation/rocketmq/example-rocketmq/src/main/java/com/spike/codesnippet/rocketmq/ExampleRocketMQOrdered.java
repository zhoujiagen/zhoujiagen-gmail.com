package com.spike.codesnippet.rocketmq;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 
 */
public class ExampleRocketMQOrdered {

  public static void main(String[] args) {
    final ExampleRocketMQOrdered example = new ExampleRocketMQOrdered();

    Thread t1 = new Thread(new Runnable() {

      @Override
      public void run() {
        example.sendOrderedMessage();
      }
    });
    t1.start();

    Thread t2 = new Thread(new Runnable() {

      @Override
      public void run() {
        example.consumeOrderedMessage();
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

  public void sendOrderedMessage() {
    DefaultMQProducer producer =
        new DefaultMQProducer(this.getClass().getSimpleName() + "_PRODUCER_GROUP");
    producer.setNamesrvAddr("127.0.0.1:9876");

    try {
      producer.start();

      String[] tags = new String[] { "TagA", "TagB", "TagC", "TagD", "TagE" };
      for (int i = 0; i < 100; i++) {
        int orderId = i % 10;

        try {
          Message message = new Message(//
              this.getClass().getSimpleName() + "_Topic", // topic
              tags[i % tags.length], // tags
              "KEY_" + i, // keys,
              ("Hello Order " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) // body
          );

          SendResult sendResult = producer.send(//
            message, // message
            new MessageQueueSelector() {
              @Override
              public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                Integer id = (Integer) arg;
                int index = id % mqs.size();
                return mqs.get(index);
              }
            }, // selector
            orderId // arg
          );
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

    } catch (

    MQClientException e) {
      e.printStackTrace();
    } finally {
      producer.shutdown();
    }

  }

  public void consumeOrderedMessage() {
    DefaultMQPushConsumer consumer =
        new DefaultMQPushConsumer(this.getClass().getSimpleName() + "_CONSUMER_GROUP");

    // 从头开始消费, 默认为从上次位置继续消费
    consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    try {
      consumer.subscribe(//
        this.getClass().getSimpleName() + "_Topic", // topic
        "TagA || TagC || TagD"// messageSelector
      );
    } catch (MQClientException e) {
      e.printStackTrace();
    }

    MessageListenerOrderly messageListener = new MessageListenerOrderly() {

      AtomicLong consumeTimes = new AtomicLong(0);

      @SuppressWarnings("deprecation")
      @Override
      public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs,
          ConsumeOrderlyContext context) {
        System.err.println(Thread.currentThread().getName() + " receive: " + msgs);
        this.consumeTimes.incrementAndGet();

        if ((consumeTimes.get() % 2 == 0)) {
          return ConsumeOrderlyStatus.SUCCESS;
        } else if ((consumeTimes.get() % 3 == 0)) {
          return ConsumeOrderlyStatus.ROLLBACK;
        } else if ((consumeTimes.get() % 2 == 0)) {
          return ConsumeOrderlyStatus.COMMIT;
        } else if ((consumeTimes.get() % 5 == 0)) {
          return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }

        return ConsumeOrderlyStatus.SUCCESS;
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
