package com.isoftstone.common.mongobackup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.isoftstone.common.jd.FaceVisitorLogDto;

@Mapper
public interface FaceVisitorLogDtoMapper extends BaseMapper<FaceVisitorLogDto, String>{
    int insertBatch(List<FaceVisitorLogDto> list);
    List<FaceVisitorLogDto> selectByTime(@Param("startTime")String startTime,@Param("endTime")String endTime);
}