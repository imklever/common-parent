package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; 

import com.isoftstone.common.jd.StatisticsDto;
import com.isoftstone.common.mongobackup.mapper.BaseMapper;
import com.isoftstone.common.mongobackup.mapper.StatisticsDtoMapper;
import com.isoftstone.common.mongobackup.service.AbstractBaseService;
import com.isoftstone.common.mongobackup.service.common.jd.StatisticsDtoService;

@Service
public class StatisticsDtoServiceImpl extends AbstractBaseService<StatisticsDto, String> 
implements StatisticsDtoService {

	@Autowired
	StatisticsDtoMapper statisticsDtoMapper;
	@Override
	protected BaseMapper<StatisticsDto, String> getMapper() { 
		return statisticsDtoMapper;
	}
	@Override
	public int insertBatch(List<StatisticsDto> list) {
		return statisticsDtoMapper.insertBatch(list);
	}
	@Override
	public StatisticsDto selectByDateTime(String unitId,String dateTime,String date) {
		return statisticsDtoMapper.selectByDateTime(unitId,dateTime,date);
	}
	@Override
	public int deleteByDateTime(String unitId, String dateTime) {
		return statisticsDtoMapper.deleteByDateTime(unitId, dateTime);
	}
}
