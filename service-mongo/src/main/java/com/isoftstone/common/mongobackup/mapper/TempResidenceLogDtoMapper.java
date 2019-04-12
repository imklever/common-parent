package com.isoftstone.common.mongobackup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.isoftstone.common.mongobackup.domain.mysql.TempResidenceLogDto;

@Mapper
public interface TempResidenceLogDtoMapper {
    int insert(TempResidenceLogDto record);
    int deleteAll();
    int insertBatch(List<TempResidenceLogDto> list);
    List<TempResidenceLogDto> selectByTempAppeared();
}