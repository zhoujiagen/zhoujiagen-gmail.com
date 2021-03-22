package com.spike.codesnippet.seata.order.service;

import com.spike.codesnippet.seata.order.domain.Order;

/**
 * 
 */
public interface OrderService {
  /**
   * 创建订单
   */
  Order create(String userId, String commodityCode, int orderCount);
}
