package com.isoftstone.common.mongobackup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.isoftstone.common.jd.FaceIdCollectionDto;

@Mapper
public interface FaceIdCollectionDtoMapper extends BaseMapper<FaceIdCollectionDto, String> {
   int insertBatch(List<FaceIdCollectionDto> list);
   int deleteAll();
   FaceIdCollectionDto selectOne();
   List<FaceIdCollectionDto> selectByTime(@Param("startTime")long startTime,@Param("endTime")long endTime);
}