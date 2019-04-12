package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.isoftstone.common.mongobackup.domain.FaceIdCollection;
import com.isoftstone.common.mongobackup.service.common.jd.FaceIdCollectionService;

@Service
public class FaceIdCollectionServiceImpl implements FaceIdCollectionService<FaceIdCollection> {

	@Autowired
	MongoTemplate mongoTemplate;
	@Override
	public List<FaceIdCollection> getByTime(long startTime,long endTime) { 
		Query query = new Query();
		query.addCriteria(Criteria.where("capture_time").gt(startTime).lte(endTime));
		return mongoTemplate.find(query, FaceIdCollection.class, "face_id_collection");		
	}

}
