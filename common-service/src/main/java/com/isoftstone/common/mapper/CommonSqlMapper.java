package com.isoftstone.common.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommonSqlMapper {
	List<Map<String, Object>> getSqlQuery(@Param(value="map")Map<String, Object> map);
	
	int getUpdateSql(@Param(value="map")Map<String, Object> map);
	
	int getInsertSql(@Param(value="map")Map<String, Object> map);
	
	int getDeleteSql(@Param(value="map")Map<String, Object> map);
	
	int getInsertSqlAndId(@Param(value="map")Map<String, Object> map);
}
