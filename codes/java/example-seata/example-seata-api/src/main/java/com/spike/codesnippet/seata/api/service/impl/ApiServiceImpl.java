package com.spike.codesnippet.seata.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spike.codesnippet.seata.api.service.ApiService;
import com.spike.codesnippet.seata.api.service.ref.DubboServiceRef;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * 
 */
@org.springframework.stereotype.Service
public class ApiServiceImpl implements ApiService {
  private static final Logger LOG = LoggerFactory.getLogger(ApiServiceImpl.class);

  @Autowired
  private DubboServiceRef dubboServiceRef;

  @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
  public void purchase(String userId, String commodityCode, int orderCount) {
    LOG.info("purchase begin ... xid: " + RootContext.getXID());

    dubboServiceRef.getStorage().deduct(commodityCode, orderCount);
    dubboServiceRef.getOrder().create(userId, commodityCode, orderCount);
  }

}
