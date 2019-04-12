package com.isoftstone.common.mongobackup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.isoftstone.common.mongobackup.domain.mysql.TempAppearedLogDto;

@Mapper
public interface TempAppearedLogDtoMapper {
    int insert(TempAppearedLogDto record);
    int insertBatch(List<TempAppearedLogDto> list);
    int deleteAll();
	List<TempAppearedLogDto> selectByType(String unitId);
	List<TempAppearedLogDto> selectByDate(@Param("unitId")String unitId,@Param("appearedDate")String appearedDate,@Param("appearedHour")String appearedHour);
}