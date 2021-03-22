package com.spike.codesnippet.seata.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class StorageStarter {
  public static void main(String[] args) {
    SpringApplication.run(StorageStarter.class, args);
  }
}
