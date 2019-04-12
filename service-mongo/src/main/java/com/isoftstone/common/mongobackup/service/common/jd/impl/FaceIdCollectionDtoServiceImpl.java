package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.jd.FaceIdCollectionDto;
import com.isoftstone.common.mongobackup.mapper.BaseMapper;
import com.isoftstone.common.mongobackup.mapper.FaceIdCollectionDtoMapper;
import com.isoftstone.common.mongobackup.service.AbstractBaseService;
import com.isoftstone.common.mongobackup.service.common.jd.FaceIdCollectionDtoService;

@Service
public class FaceIdCollectionDtoServiceImpl extends AbstractBaseService<FaceIdCollectionDto, String> implements
		FaceIdCollectionDtoService {

	@Autowired
	FaceIdCollectionDtoMapper faceIdCollectionDtoMapper;
	
	@Override
	protected BaseMapper<FaceIdCollectionDto, String> getMapper() { 
		return faceIdCollectionDtoMapper;
	}
	
	@Override
	public int insertBatch(List<FaceIdCollectionDto> list) {		 
		return faceIdCollectionDtoMapper.insertBatch(list);

	}

	@Override
	public int deleteAll() {
		return faceIdCollectionDtoMapper.deleteAll();
	}

	@Override
	public FaceIdCollectionDto selectOne() {
		return faceIdCollectionDtoMapper.selectOne();
	}

	@Override
	public List<FaceIdCollectionDto> selectByTime(long startTime,
			long endTime) {
		return faceIdCollectionDtoMapper.selectByTime(startTime, endTime);
	} 
}
