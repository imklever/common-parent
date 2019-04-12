package com.isoftstone.common.api.service.common;

import javax.servlet.http.HttpServletRequest;

public interface SysLogService {
	void log(HttpServletRequest request);
}
