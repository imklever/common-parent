package com.isoftstone.common.api.support.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.isoftstone.common.api.service.common.PermissionService;

@Service
public class PermissionsInterceptor implements HandlerInterceptor{
	@Autowired
	PermissionService PermissionService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	return PermissionService.hasPermission(request,response);
    }
	

}
