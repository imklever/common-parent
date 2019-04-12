package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.jd.FaceLogCollectionDto;
import com.isoftstone.common.mongobackup.mapper.BaseMapper;
import com.isoftstone.common.mongobackup.mapper.FaceLogCollectionDtoMapper;
import com.isoftstone.common.mongobackup.service.AbstractBaseService;
import com.isoftstone.common.mongobackup.service.common.jd.FaceLogCollectionDtoService;

@Service
public class FaceLogCollectionDtoServiceImpl extends AbstractBaseService<FaceLogCollectionDto, String> implements
FaceLogCollectionDtoService {

	@Autowired
	FaceLogCollectionDtoMapper faceLogCollectionDtoMapper;
	
	@Override
	protected BaseMapper<FaceLogCollectionDto, String> getMapper() { 
		return faceLogCollectionDtoMapper;
	}
	
	@Override
	public int insertBatch(List<FaceLogCollectionDto> list) {		 
		return faceLogCollectionDtoMapper.insertBatch(list);
	}

	@Override
	public int deleteAll() {
		return faceLogCollectionDtoMapper.deleteAll();
	}

	@Override
	public FaceLogCollectionDto selectOne() {
		return faceLogCollectionDtoMapper.selectOne();
	}

	@Override
	public int deleteByDate(String appearedDate) {
		return faceLogCollectionDtoMapper.deleteByDate(appearedDate);
	}

	@Override
	public List<FaceLogCollectionDto> selectLogDates(String unitId) {
	    return faceLogCollectionDtoMapper.selectLogDates(unitId);
	}

	@Override
	public List<FaceLogCollectionDto> selectByTime(long startTime,
			long endTime) {
		return faceLogCollectionDtoMapper.selectByTime(startTime, endTime);
	}

	@Override
	public int deleteFromStartToEnd(long startTime, long endTime) {
		return faceLogCollectionDtoMapper.deleteFromStartToEnd(startTime, endTime);
	} 
}
