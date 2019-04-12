package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

import com.isoftstone.common.mongobackup.domain.mysql.TempResidenceLogDto;

public interface TempResidenceLogDtoService {
	int insertBatch(List<TempResidenceLogDto> list);
	int deleteAll();
	List<TempResidenceLogDto> selectByTempAppeared();
}
