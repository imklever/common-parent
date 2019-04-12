package com.isoftstone.common.mongobackup.mapper;

import org.apache.ibatis.annotations.Mapper;
 

import  com.isoftstone.common.jd.SyncScheduleDto;

@Mapper
public interface SyncScheduleDtoMapper  extends BaseMapper<SyncScheduleDto, String> {
	int deleteAll();
	SyncScheduleDto selectOne();
}