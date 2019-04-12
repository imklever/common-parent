package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

import com.isoftstone.common.jd.FaceVisitorLogDto;
import com.isoftstone.common.mongobackup.service.BaseService;
 
public interface FaceVisitorLogDtoService extends BaseService<FaceVisitorLogDto, String>{
	int insertBatch(List<FaceVisitorLogDto> list);
	List<FaceVisitorLogDto> selectByTime(String startTime,String endTime);
}
