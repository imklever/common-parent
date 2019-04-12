package com.isoftstone.common.mongobackup.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.isoftstone.common.jd.HourStatisticsDto;

@Mapper
public interface HourStatisticsDtoMapper extends BaseMapper<HourStatisticsDto, String>{
	List<HourStatisticsDto> selectByDateTime(Map<?, ?> paramMap);
	int deleteByDateTime(Map<?, ?> paramMap);
	int insertBatch(List<HourStatisticsDto> list);
}