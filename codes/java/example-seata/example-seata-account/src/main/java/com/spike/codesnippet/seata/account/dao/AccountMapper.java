package com.spike.codesnippet.seata.account.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.spike.codesnippet.seata.account.domain.Account;

@Repository
public interface AccountMapper {
  int deleteByPrimaryKey(Integer id);

  int insert(Account record);

  int debit(@Param("userId") String userId, @Param("money") int money);

  Account selectByPrimaryKey(Integer id);

  List<Account> selectAll();

  int updateByPrimaryKey(Account record);
}