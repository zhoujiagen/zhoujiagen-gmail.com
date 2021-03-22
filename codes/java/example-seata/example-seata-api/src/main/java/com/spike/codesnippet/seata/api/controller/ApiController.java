package com.spike.codesnippet.seata.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spike.codesnippet.seata.api.domain.request.PurchaseRequest;
import com.spike.codesnippet.seata.api.service.ApiService;

/**
 * 
 */
@RestController
@RequestMapping("/api")
public class ApiController {

  @Autowired
  private ApiService apiService;

  @RequestMapping(value = "/purchase", method = { RequestMethod.POST })
  public String purchase(@RequestBody PurchaseRequest purchaseRequest) {
    apiService.purchase(purchaseRequest.getUserId(), purchaseRequest.getCommodityCode(),
      purchaseRequest.getOrderCount());
    return "ok";
  }
}
