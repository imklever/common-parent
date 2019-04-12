package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.jd.FaceVisitorLogDto;
import com.isoftstone.common.mongobackup.mapper.BaseMapper;
import com.isoftstone.common.mongobackup.mapper.FaceVisitorLogDtoMapper;
import com.isoftstone.common.mongobackup.service.AbstractBaseService;
import com.isoftstone.common.mongobackup.service.common.jd.FaceVisitorLogDtoService;

@Service
public class FaceVisitorLogDtoServiceImpl extends AbstractBaseService<FaceVisitorLogDto, String> implements
FaceVisitorLogDtoService {

	@Autowired
	FaceVisitorLogDtoMapper faceVisitorLogDtoMapper;
	
	@Override
	protected BaseMapper<FaceVisitorLogDto, String> getMapper() { 
		return faceVisitorLogDtoMapper;
	}

	@Override
	public int insertBatch(List<FaceVisitorLogDto> list) {
		return faceVisitorLogDtoMapper.insertBatch(list);
	}

	@Override
	public List<FaceVisitorLogDto> selectByTime(String startTime, String endTime) {
		return faceVisitorLogDtoMapper.selectByTime(startTime, endTime);
	}
 
}
