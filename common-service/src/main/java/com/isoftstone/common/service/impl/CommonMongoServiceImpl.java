package com.isoftstone.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;

import com.isoftstone.common.common.MongoSqlDto;
import com.isoftstone.common.service.CommonMongoService;
import com.isoftstone.common.util.JsonService;
import com.mongodb.client.result.DeleteResult;
@Service
public class CommonMongoServiceImpl implements CommonMongoService{
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private JsonService jsonService;
    @Override
    public List<Map> find(MongoSqlDto mongoSqlDto) {
        BasicQuery query = new BasicQuery(mongoSqlDto.getQuery());
        List<Map> list =mongoTemplate.find(query, Map.class, mongoSqlDto.getTable());
        return list;
    }
    @Override
    public Map<String, Object> remove(MongoSqlDto mongoSqlDto) {
        BasicQuery query = new BasicQuery(mongoSqlDto.getQuery());
        DeleteResult deleteResult = mongoTemplate.remove(query, mongoSqlDto.getTable());
        return  jsonService.parseMap(jsonService.toJson(deleteResult));
    }
    @Override
    public void insert(MongoSqlDto mongoSqlDto) {
        mongoTemplate.insert(
                jsonService.parseObject(mongoSqlDto.getInsert())
                , mongoSqlDto.getTable());
    }

}
