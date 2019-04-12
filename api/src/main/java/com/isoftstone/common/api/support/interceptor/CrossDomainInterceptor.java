package com.isoftstone.common.api.support.interceptor;

import com.isoftstone.common.util.MyProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
public class CrossDomainInterceptor implements HandlerInterceptor {
	  @Autowired
      MyProps mapProps;
	  @Override
	    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
	        //if (mapProps.isLocal()) {
	            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
	        //}
	            System.out.println("------------------------");
	        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*,token");
	        return true;
	    }

	    @Override
	    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, 
	    		Object o, ModelAndView modelAndView) throws Exception {

	    }

	    @Override
	    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
	    }
}
