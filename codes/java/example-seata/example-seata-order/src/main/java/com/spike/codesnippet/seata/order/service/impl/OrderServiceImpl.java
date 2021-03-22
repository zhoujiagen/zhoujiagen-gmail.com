package com.spike.codesnippet.seata.order.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.spike.codesnippet.seata.order.dao.OrderMapper;
import com.spike.codesnippet.seata.order.domain.Order;
import com.spike.codesnippet.seata.order.service.OrderService;
import com.spike.codesnippet.seata.order.service.ref.AccountServiceRef;

import io.seata.core.context.RootContext;

/**
 * 
 */
@org.apache.dubbo.config.annotation.Service
public class OrderServiceImpl implements OrderService {
  private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

  @Autowired
  private AccountServiceRef accountService;

  @Autowired
  private OrderMapper orderMapper;

  /**
   * 扣除存储数量
   */
  @Transactional
  public Order create(String userId, String commodityCode, int orderCount) {
    LOG.info("Order Service Begin ... xid: " + RootContext.getXID());

    int orderMoney = calculate(commodityCode, orderCount);

    Order result = new Order();
    result.setUserId(userId);
    result.setCommodityCode(commodityCode);
    result.setCount(orderCount);
    result.setMoney(orderMoney);

    orderMapper.insert(result);

    accountService.get().debit(userId, orderMoney);

    LOG.info("Order Service End ... Created " + result);

    return result;
  }

  private int calculate(String commodityCode, int orderCount) {
    return 200 * orderCount;
  }

}
