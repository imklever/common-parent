package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.mongobackup.domain.mysql.TempHourDto;
import com.isoftstone.common.mongobackup.mapper.TempHourDtoMapper;
import com.isoftstone.common.mongobackup.service.common.jd.TempHourDtoService;

@Service
public class TempHourDtoServiceImpl implements TempHourDtoService {

	@Autowired
	TempHourDtoMapper tempHourDtoMapper;
	@Override
	public List<TempHourDto> selectAll(String unitId) {
		return tempHourDtoMapper.selectAll(unitId);
	}
}
