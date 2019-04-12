package com.isoftstone.common.api.service.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PermissionService {
	boolean hasPermission(HttpServletRequest request, HttpServletResponse response)throws Exception;	
}
