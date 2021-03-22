package com.spike.codesnippet.seata.storage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.spike.codesnippet.seata.storage.domain.Storage;

@Repository
public interface StorageMapper {
  int deleteByPrimaryKey(Integer id);

  int insert(Storage record);

  int deduct(@Param("commodityCode") String commodityCode, @Param("count") int count);

  Storage selectByPrimaryKey(Integer id);

  List<Storage> selectAll();

  int updateByPrimaryKey(Storage record);
}