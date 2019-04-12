package com.isoftstone.common.mongobackup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.isoftstone.common.jd.StatisticsDto;

@Mapper
public interface StatisticsDtoMapper extends BaseMapper<StatisticsDto, String> {
	int insertBatch(List<StatisticsDto> statisticsDtos);
	StatisticsDto selectByDateTime(@Param("unitId")String unitId,@Param("dateTime")String dateTime,@Param("date")String date);
	int deleteByDateTime(@Param("unitId")String unitId,@Param("dateTime")String dateTime);
}