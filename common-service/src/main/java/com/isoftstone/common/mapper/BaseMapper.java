package com.isoftstone.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.isoftstone.common.IDEntity;

@Mapper
public interface BaseMapper <T extends IDEntity, ID extends String> {
    int deleteByPrimaryKey(ID id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(String string);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
