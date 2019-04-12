package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

public interface LogCollectionService<T> {
	List<T> getByTime(long startTime,long endTime); 
}