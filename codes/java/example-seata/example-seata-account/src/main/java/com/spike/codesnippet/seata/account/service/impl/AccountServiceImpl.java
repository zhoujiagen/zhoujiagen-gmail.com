package com.spike.codesnippet.seata.account.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spike.codesnippet.seata.account.dao.AccountMapper;
import com.spike.codesnippet.seata.account.service.AccountService;

import io.seata.core.context.RootContext;

/**
 * 
 */
@org.apache.dubbo.config.annotation.Service
public class AccountServiceImpl implements AccountService {
  private static final Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);

  private static final String ERROR_USER_ID = "1002";

  @Autowired
  private AccountMapper accountMapper;

  /**
   * 扣除存储数量
   */
  public void debit(String userId, int money) {
    LOG.info("Account Service ... xid: " + RootContext.getXID());
    accountMapper.debit(userId, money);

    if (ERROR_USER_ID.equals(userId)) {
      LOG.info("Account Service End with exception... ");
      throw new RuntimeException("account branch exception");
    }

    LOG.info("Account Service End ... ");
  }
}
