package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.isoftstone.common.jd.FaceIdCollectionDto;
import com.isoftstone.common.jd.FaceLogCollectionDto;
import com.isoftstone.common.mongobackup.service.BaseService;
 
public interface FaceLogCollectionDtoService extends BaseService<FaceLogCollectionDto, String>{
	int insertBatch(List<FaceLogCollectionDto> list);
	int deleteAll();
	FaceLogCollectionDto selectOne();
	int deleteByDate(String appearedDate);
	List<FaceLogCollectionDto> selectLogDates(String unitId);
	List<FaceLogCollectionDto> selectByTime(long startTime,long endTime);
	int deleteFromStartToEnd(long startTime,long endTime);
}
