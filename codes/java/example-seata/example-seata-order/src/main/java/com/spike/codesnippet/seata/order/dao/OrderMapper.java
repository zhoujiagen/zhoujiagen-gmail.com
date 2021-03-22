package com.spike.codesnippet.seata.order.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spike.codesnippet.seata.order.domain.Order;

@Repository
public interface OrderMapper {
  int deleteByPrimaryKey(Integer id);

  int insert(Order record);

  Order selectByPrimaryKey(Integer id);

  List<Order> selectAll();

  int updateByPrimaryKey(Order record);
}