package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

import com.isoftstone.common.mongobackup.domain.mysql.FaceIdTypeCountDto;

public interface FaceIdTypeCountDtoService {
	List<FaceIdTypeCountDto> selectTypeCount(String unitId);
	List<FaceIdTypeCountDto> selectTypeCount(String unitId,String appearedDate,String appearedHour);
}
