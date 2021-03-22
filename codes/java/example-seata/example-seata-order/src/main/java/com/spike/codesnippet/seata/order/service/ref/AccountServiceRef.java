package com.spike.codesnippet.seata.order.service.ref;

import org.springframework.stereotype.Component;

import com.spike.codesnippet.seata.account.service.AccountService;

@Component
public class AccountServiceRef {
  @org.apache.dubbo.config.annotation.Reference
  private AccountService accountService;

  public AccountService get() {
    return accountService;
  }

}
