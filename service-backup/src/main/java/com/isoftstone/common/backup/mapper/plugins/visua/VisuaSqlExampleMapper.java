package com.isoftstone.common.backup.mapper.plugins.visua;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.isoftstone.common.mapper.BaseMapper;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
@Mapper
public interface VisuaSqlExampleMapper extends BaseMapper<VisuaSqlExample, String>{

	VisuaSqlExample getByBusinessCode(@Param(value="businesscode") String businesscode);

	int getByBusinessCodeCount(@Param(value="businesscode")String businessCode);

	List<VisuaSqlExample> findBy(@Param(value="visuaSqlExample")VisuaSqlExample visuaSqlExample
				,@Param(value="sort") List<String> sort);
	
	List<VisuaSqlExample> findByUserId(String userId);
}