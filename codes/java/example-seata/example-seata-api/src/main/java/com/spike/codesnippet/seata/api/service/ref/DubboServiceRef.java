package com.spike.codesnippet.seata.api.service.ref;

import org.springframework.stereotype.Component;

import com.spike.codesnippet.seata.order.service.OrderService;
import com.spike.codesnippet.seata.storage.service.StorageService;

/**
 * 
 */
@Component
public class DubboServiceRef {
  @org.apache.dubbo.config.annotation.Reference
  private StorageService storageService;

  @org.apache.dubbo.config.annotation.Reference
  private OrderService orderService;

  public StorageService getStorage() {
    return storageService;
  }

  public OrderService getOrder() {
    return orderService;
  }

}
