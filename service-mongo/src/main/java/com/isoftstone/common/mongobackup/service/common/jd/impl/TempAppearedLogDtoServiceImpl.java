package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.mongobackup.domain.mysql.TempAppearedLogDto;
import com.isoftstone.common.mongobackup.mapper.TempAppearedLogDtoMapper;
import com.isoftstone.common.mongobackup.service.common.jd.TempAppearedLogDtoService;

@Service
public class TempAppearedLogDtoServiceImpl implements TempAppearedLogDtoService {

	@Autowired
	TempAppearedLogDtoMapper tempAppearedLogDtoMapper;

	@Override
	public int insertBatch(List<TempAppearedLogDto> list) {
	   return tempAppearedLogDtoMapper.insertBatch(list);
	}

	@Override
	public List<TempAppearedLogDto> selectByType(String unitId) {
		return tempAppearedLogDtoMapper.selectByType(unitId);
	}

	@Override
	public int deleteAll() {
		return tempAppearedLogDtoMapper.deleteAll();
	}

	@Override
	public List<TempAppearedLogDto> selectByDate(String unitId,
			String appearedDate, String appearedHour) {
		return tempAppearedLogDtoMapper.selectByDate(unitId, appearedDate, appearedHour);
	}

 
}
