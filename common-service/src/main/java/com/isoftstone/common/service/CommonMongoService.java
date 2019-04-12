package com.isoftstone.common.service;

import java.util.List;
import java.util.Map;

import com.isoftstone.common.common.MongoSqlDto;

public interface CommonMongoService {

    List<Map> find(MongoSqlDto mongoSqlDto);

    Map<String, Object> remove(MongoSqlDto mongoSqlDto);

    void insert(MongoSqlDto mongoSqlDto);

}
