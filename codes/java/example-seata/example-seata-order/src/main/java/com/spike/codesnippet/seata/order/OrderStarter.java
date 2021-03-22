package com.spike.codesnippet.seata.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class OrderStarter {
  public static void main(String[] args) {
    SpringApplication.run(OrderStarter.class, args);
  }
}
