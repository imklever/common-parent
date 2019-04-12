package com.isoftstone.common.backup.mapper.common;

import org.apache.ibatis.annotations.Mapper;

import com.isoftstone.common.common.SysExceptionLogDto;
import com.isoftstone.common.mapper.BaseMapper;

@Mapper
public interface SysExceptionDtoMapper extends BaseMapper<SysExceptionLogDto, String> {
  
}