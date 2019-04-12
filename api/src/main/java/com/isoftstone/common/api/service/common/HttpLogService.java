package com.isoftstone.common.api.service.common;

import javax.servlet.http.HttpServletRequest;

public interface HttpLogService {
	void log(HttpServletRequest request, int time);
}
