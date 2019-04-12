package com.isoftstone.common.mongobackup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.isoftstone.common.jd.FaceLogCollectionDto;

@Mapper
public interface FaceLogCollectionDtoMapper  extends BaseMapper<FaceLogCollectionDto, String> {
	   int insertBatch(List<FaceLogCollectionDto> list);
	   int deleteAll();
	   FaceLogCollectionDto selectOne();
	   int deleteByDate(String appearedDate);
	   List<FaceLogCollectionDto> selectLogDates(String unitId);
	   List<FaceLogCollectionDto> selectByTime(@Param("startTime")long startTime,@Param("endTime")long endTime);
	   int deleteFromStartToEnd(@Param("startTime")long startTime,@Param("endTime")long endTime);
}