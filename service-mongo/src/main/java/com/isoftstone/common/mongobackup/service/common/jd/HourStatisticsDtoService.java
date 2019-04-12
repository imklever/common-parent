package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

import com.isoftstone.common.jd.HourStatisticsDto;
import com.isoftstone.common.mongobackup.service.BaseService;

public interface HourStatisticsDtoService  extends BaseService<HourStatisticsDto, String> {
	List<HourStatisticsDto> selectByDateTime(String dateTime,String unitId);
	 int insert(HourStatisticsDto hourStatisticsDto);
	 int update(HourStatisticsDto hourStatisticsDto);
	 int deleteByDateTime(String dateTime,String unitId);
	 int insertBatch(List<HourStatisticsDto> list);
}