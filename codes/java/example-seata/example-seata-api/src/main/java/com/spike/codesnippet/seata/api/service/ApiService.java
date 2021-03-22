package com.spike.codesnippet.seata.api.service;

/**
 * 
 */
public interface ApiService {

  public void purchase(String userId, String commodityCode, int orderCount);
}
