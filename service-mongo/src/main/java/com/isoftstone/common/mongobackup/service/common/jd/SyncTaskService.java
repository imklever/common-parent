package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.Date;

public interface SyncTaskService {
	void sync();
	void sync(Date startDateTime,Date endDateTime);
}
