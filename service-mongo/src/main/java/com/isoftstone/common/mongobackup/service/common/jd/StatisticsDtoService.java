package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

import com.isoftstone.common.jd.StatisticsDto;
import com.isoftstone.common.mongobackup.service.BaseService;

public interface StatisticsDtoService  extends BaseService<StatisticsDto, String> {
	int insertBatch(List<StatisticsDto> list); 
	StatisticsDto selectByDateTime(String unitId,String dateTime,String date);
	int deleteByDateTime(String unitId,String dateTime);
}