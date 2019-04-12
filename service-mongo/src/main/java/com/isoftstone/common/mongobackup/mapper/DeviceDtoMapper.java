package com.isoftstone.common.mongobackup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.isoftstone.common.mongobackup.domain.mysql.DeviceDto;

@Mapper
public interface DeviceDtoMapper extends BaseMapper<DeviceDto, String>{
	List<DeviceDto> selectAll();

	List<DeviceDto> selectByTypeId(String typeId);
}