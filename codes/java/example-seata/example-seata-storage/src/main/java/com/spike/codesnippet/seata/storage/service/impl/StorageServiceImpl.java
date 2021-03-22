package com.spike.codesnippet.seata.storage.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.spike.codesnippet.seata.storage.dao.StorageMapper;
import com.spike.codesnippet.seata.storage.domain.Storage;
import com.spike.codesnippet.seata.storage.service.StorageService;

import io.seata.core.context.RootContext;

/**
 * 
 */
@org.apache.dubbo.config.annotation.Service
public class StorageServiceImpl implements StorageService {
  private static final Logger LOG = LoggerFactory.getLogger(StorageServiceImpl.class);

  @Autowired
  private StorageMapper storageMapper;

  /**
   * 扣除存储数量
   */
  @Transactional
  public void deduct(String commodityCode, int count) {
    LOG.info("Storage Service Begin ... xid: " + RootContext.getXID());

    storageMapper.insert(new Storage());

    LOG.info("Storage Service End ... ");

  }
}
