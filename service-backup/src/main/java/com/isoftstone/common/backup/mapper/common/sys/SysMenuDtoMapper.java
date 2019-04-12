package com.isoftstone.common.backup.mapper.common.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.isoftstone.common.common.sys.SysMenuDto;
import com.isoftstone.common.mapper.BaseMapper;

@Mapper
public interface SysMenuDtoMapper  extends BaseMapper<SysMenuDto, String>{
	List<SysMenuDto> findByIds(List<String> menuList);
}