package com.isoftstone.common.task.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.isoftstone.common.mapper.BaseMapper;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;

@Mapper
public interface VisuaSqlExampleMapper extends BaseMapper<VisuaSqlExample, String>{
    VisuaSqlExample getByBusinessCode(@Param(value="businesscode") String businesscode);
}
