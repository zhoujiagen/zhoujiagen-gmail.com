package com.spike.codesnippet.seata.api.domain.request;

import java.io.Serializable;

/**
 * 
 */
public class PurchaseRequest implements Serializable {
  private static final long serialVersionUID = 302537903560214779L;
  private String userId;
  private String commodityCode;
  private int orderCount;

  public PurchaseRequest() {
    super();
  }

  public PurchaseRequest(String userId, String commodityCode, int orderCount) {
    this.userId = userId;
    this.commodityCode = commodityCode;
    this.orderCount = orderCount;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getCommodityCode() {
    return commodityCode;
  }

  public void setCommodityCode(String commodityCode) {
    this.commodityCode = commodityCode;
  }

  public int getOrderCount() {
    return orderCount;
  }

  public void setOrderCount(int orderCount) {
    this.orderCount = orderCount;
  }

}
