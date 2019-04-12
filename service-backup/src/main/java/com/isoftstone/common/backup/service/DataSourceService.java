package com.isoftstone.common.backup.service;

public interface DataSourceService {
	void switchDataSource(String dataSource);
	void restoreDataSource();
}
