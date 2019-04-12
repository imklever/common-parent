package com.isoftstone.common.backup.service.plugins.visua.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.isoftstone.common.backup.mapper.plugins.visua.VisuaSqlExampleVerMapper;
import com.isoftstone.common.backup.service.AbstractBaseService;
import com.isoftstone.common.backup.service.plugins.visua.VisuaSqlExampleVerService;
import com.isoftstone.common.mapper.BaseMapper;
import com.isoftstone.common.plugins.visua.VisuaSqlExampleVer;


@Service
// @Transactional
public class VisuaSqlExampleVerServiceImpl extends AbstractBaseService<VisuaSqlExampleVer, String>
		implements VisuaSqlExampleVerService {
	@Autowired
	VisuaSqlExampleVerMapper visuaSqlExampleVerMapper;
	
	@Override
	protected BaseMapper<VisuaSqlExampleVer, String> getMapper() {
		return visuaSqlExampleVerMapper;
	}
/**
 * 版本信息
 */
	@Override
	public int saveVersion(VisuaSqlExampleVer visuaSqlExampleVer) {
		return visuaSqlExampleVerMapper.insert(visuaSqlExampleVer);
	}

	
}
