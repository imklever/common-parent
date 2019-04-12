package com.isoftstone.common.api.support.interceptor;

import com.isoftstone.common.api.service.common.HttpLogService;
import com.isoftstone.common.api.service.common.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class HttpLogInterceptor implements HandlerInterceptor{
	private static final NamedThreadLocal<Long> trackStartTimeThreadLocal = new NamedThreadLocal<Long>("trackStartTimeThreadLocal");
	@Autowired
	HttpLogService httpLogService;
	@Autowired
	SysLogService sysLogService;
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    	//用户日志记录
		sysLogService.log(request);
		//用户http日志记录
        httpLogService.log(request, getTime());

        //删除线程变量中的数据，防止内存泄漏
        trackStartTimeThreadLocal.remove();
    }

    private int getTime() {
        return (int) (System.currentTimeMillis() - trackStartTimeThreadLocal.get());
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        trackStartTimeThreadLocal.set(System.currentTimeMillis());
        return true;
    }
	 

}
