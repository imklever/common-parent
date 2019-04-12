package com.isoftstone.common.backup.mapper.common.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.isoftstone.common.common.sys.SysJdbcDto;
import com.isoftstone.common.mapper.BaseMapper;
@Mapper
public interface SysJdbcDtoMapper  extends BaseMapper<SysJdbcDto, String>{

	List<SysJdbcDto> findAll();
}