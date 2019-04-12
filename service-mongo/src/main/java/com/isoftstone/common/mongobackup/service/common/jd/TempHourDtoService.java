package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

import com.isoftstone.common.mongobackup.domain.mysql.TempHourDto;
 
public interface TempHourDtoService {
	List<TempHourDto> selectAll(String unitId);
}
