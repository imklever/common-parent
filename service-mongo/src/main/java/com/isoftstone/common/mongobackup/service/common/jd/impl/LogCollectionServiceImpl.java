package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.isoftstone.common.mongobackup.domain.LogCollection;
import com.isoftstone.common.mongobackup.service.common.jd.LogCollectionService;

@Service
public class LogCollectionServiceImpl implements LogCollectionService<LogCollection> {
	@Autowired
	MongoTemplate mongoTemplate;
	@Override
	public List<LogCollection> getByTime(long startTime,long endTime) {  
			Query query = new Query();
			query.addCriteria(Criteria.where("appeared_time").gt(startTime).lte(endTime));	
			return mongoTemplate.find(query, LogCollection.class, "log_collection");		
	}

}
