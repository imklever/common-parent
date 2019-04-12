package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

import com.isoftstone.common.mongobackup.domain.mysql.TempAppearedLogDto;
 
public interface TempAppearedLogDtoService{
	int insertBatch(List<TempAppearedLogDto> list);
	List<TempAppearedLogDto> selectByType(String unitId);
	int deleteAll();
	List<TempAppearedLogDto> selectByDate(String unitId,String appearedDate,String appearedHour);
}
