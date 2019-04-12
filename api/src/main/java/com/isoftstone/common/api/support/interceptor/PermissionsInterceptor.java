package com.isoftstone.common.api.support.interceptor;

import com.isoftstone.common.api.service.common.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class PermissionsInterceptor implements HandlerInterceptor{
	@Autowired
	PermissionService PermissionService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	return PermissionService.hasPermission(request,response);
    }
	

}
