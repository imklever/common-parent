package com.isoftstone.common.backup.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.isoftstone.common.backup.service.DataSourceService;
import com.isoftstone.common.datasource.DynamicDataSourceContextHolder;
import com.isoftstone.common.datasource.DynamicRoutingDataSource;

//@Service
public class DataSourceServiceImpl implements DataSourceService{
	@Resource
	DynamicRoutingDataSource dynamicDataSource;
	@Override
	public void switchDataSource(String dataSource) {
			if (!DynamicDataSourceContextHolder.containDataSourceKey(dataSource)) {
				//to-do  动态添加数据源
				//DynamicRoutingDataSourceAddDataSource();
	        }
            // 切换数据源
            DynamicDataSourceContextHolder.setDataSourceKey(dataSource);
	}
	/**
	 * 
	 */
	private void DynamicRoutingDataSourceAddDataSource() {
		DynamicDataSourceContextHolder.clearDataSourceKey();
		Map<Object, Object> dataSourceMap = new HashMap<>();
		
		
		dynamicDataSource.setTargetDataSources(dataSourceMap);
	}

	@Override
	public void restoreDataSource() {
		 DynamicDataSourceContextHolder.clearDataSourceKey();
	}

}
