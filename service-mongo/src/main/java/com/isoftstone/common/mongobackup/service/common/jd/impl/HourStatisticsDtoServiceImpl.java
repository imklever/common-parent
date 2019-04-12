package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; 

import com.isoftstone.common.jd.HourStatisticsDto;
import com.isoftstone.common.mongobackup.mapper.BaseMapper;
import com.isoftstone.common.mongobackup.mapper.HourStatisticsDtoMapper;
import com.isoftstone.common.mongobackup.service.AbstractBaseService;
import com.isoftstone.common.mongobackup.service.common.jd.HourStatisticsDtoService;

@Service
public class HourStatisticsDtoServiceImpl extends AbstractBaseService<HourStatisticsDto, String> 
implements HourStatisticsDtoService {

	@Autowired
	HourStatisticsDtoMapper hourStatisticsDtoMapper;
	@Override
	protected BaseMapper<HourStatisticsDto, String> getMapper() { 
		return hourStatisticsDtoMapper;
	}
	@Override
	public List<HourStatisticsDto> selectByDateTime(String dateTime,String unitId) {
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("dateTime", dateTime);
		map.put("unitId", unitId);
		return hourStatisticsDtoMapper.selectByDateTime(map);
	}
	@Override
	public int insert(HourStatisticsDto hourStatisticsDto) {
		return hourStatisticsDtoMapper.insert(hourStatisticsDto);
	}
	@Override
	public int update(HourStatisticsDto hourStatisticsDto) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int deleteByDateTime(String dateTime,String unitId) {
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("dateTime", dateTime);
		map.put("unitId", unitId);
		return hourStatisticsDtoMapper.deleteByDateTime(map);
	}
	@Override
	public int insertBatch(List<HourStatisticsDto> list) {
		return hourStatisticsDtoMapper.insertBatch(list);
	} 
}
