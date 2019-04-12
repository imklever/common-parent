package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.mongobackup.domain.mysql.TempResidenceLogDto;
import com.isoftstone.common.mongobackup.mapper.TempResidenceLogDtoMapper;
import com.isoftstone.common.mongobackup.service.common.jd.TempResidenceLogDtoService;

@Service
public class TempResidenceLogDtoServiceImpl implements  TempResidenceLogDtoService {

	@Autowired
	 TempResidenceLogDtoMapper  tempResidenceLogDtoMapper;

	@Override
	public int insertBatch(List<TempResidenceLogDto> list) {
		return tempResidenceLogDtoMapper.insertBatch(list);
	}

	@Override
	public int deleteAll() {
		return tempResidenceLogDtoMapper.deleteAll();
	}

	@Override
	public List<TempResidenceLogDto> selectByTempAppeared() {
		return tempResidenceLogDtoMapper.selectByTempAppeared();
	} 
 
}
