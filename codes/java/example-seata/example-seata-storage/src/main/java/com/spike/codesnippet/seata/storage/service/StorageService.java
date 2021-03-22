package com.spike.codesnippet.seata.storage.service;

/**
 * 
 */
public interface StorageService {
  public void deduct(String commodityCode, int count);
}
