package com.isoftstone.common.mongobackup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.isoftstone.common.mongobackup.domain.mysql.FaceIdTypeCountDto;

@Mapper
public interface FaceIdTypeCountDtoMapper {
   List<FaceIdTypeCountDto> selectTypeCount(String unitId);
   List<FaceIdTypeCountDto> selectTypeCount(@Param("unitId")String unitId,@Param("appearedDate")String appearedDate,@Param("appearedHour")String appearedHour);   
}
