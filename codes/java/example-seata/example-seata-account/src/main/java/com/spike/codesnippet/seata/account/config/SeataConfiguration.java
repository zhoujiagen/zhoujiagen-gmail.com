package com.spike.codesnippet.seata.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.seata.spring.annotation.GlobalTransactionScanner;

/**
 * 
 */
@Configuration
public class SeataConfiguration {

  @Bean
  public GlobalTransactionScanner globalTransactionScanner() {
    return new GlobalTransactionScanner("account-app", "my_test_tx_group");
  }
}
