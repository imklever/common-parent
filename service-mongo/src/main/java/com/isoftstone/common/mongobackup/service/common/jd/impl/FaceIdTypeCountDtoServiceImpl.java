package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.mongobackup.domain.mysql.FaceIdTypeCountDto;
import com.isoftstone.common.mongobackup.mapper.FaceIdTypeCountDtoMapper;
import com.isoftstone.common.mongobackup.service.common.jd.FaceIdTypeCountDtoService;

@Service
public class FaceIdTypeCountDtoServiceImpl implements FaceIdTypeCountDtoService {

	@Autowired
	FaceIdTypeCountDtoMapper faceIdTypeCountDtoMapper;

	@Override
	public List<FaceIdTypeCountDto> selectTypeCount(String unitId) {
		return faceIdTypeCountDtoMapper.selectTypeCount(unitId);
	}

	@Override
	public List<FaceIdTypeCountDto> selectTypeCount(String unitId,
			String appearedDate, String appearedHour) {
		return faceIdTypeCountDtoMapper.selectTypeCount(unitId, appearedDate, appearedHour);
	}
	
 
}
