package com.isoftstone.common.mongobackup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.isoftstone.common.mongobackup.domain.mysql.TempHourDto;

@Mapper
public interface TempHourDtoMapper {
	List<TempHourDto> selectAll(String unitId);
}