package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

import com.isoftstone.common.jd.FaceIdCollectionDto;
import com.isoftstone.common.mongobackup.service.BaseService;
 
public interface FaceIdCollectionDtoService extends BaseService<FaceIdCollectionDto, String>{
	int insertBatch(List<FaceIdCollectionDto> list);
	int deleteAll();
	FaceIdCollectionDto selectOne();
	List<FaceIdCollectionDto> selectByTime(long startTime,long endTime);
}
