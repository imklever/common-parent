package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

public interface FaceIdCollectionService<T> {
	List<T> getByTime(long startTime,long endTime); 
}
