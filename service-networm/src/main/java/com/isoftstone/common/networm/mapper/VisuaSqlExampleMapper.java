package com.isoftstone.common.networm.mapper;

import com.isoftstone.common.mapper.BaseMapper;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VisuaSqlExampleMapper extends BaseMapper<VisuaSqlExample, String>{
    VisuaSqlExample getByBusinessCode(@Param(value = "businesscode") String businesscode);
}
