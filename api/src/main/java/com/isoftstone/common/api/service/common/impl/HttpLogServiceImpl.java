package com.isoftstone.common.api.service.common.impl;

import com.isoftstone.common.api.domain.comon.HttpRequestMeta;
import com.isoftstone.common.api.service.common.ExceptionLogService;
import com.isoftstone.common.api.service.common.HttpLogService;
import com.isoftstone.common.api.util.HttpRequestUtils;
import com.isoftstone.common.common.SysHttpLogDto;
import com.isoftstone.common.common.hystrix.HystrixSysHttpLogServiceClient;
import com.isoftstone.common.util.IdService;
import com.isoftstone.common.util.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class HttpLogServiceImpl implements HttpLogService{
	@Autowired
    JsonService jsonService;
	@Autowired
	ExceptionLogService exceptionLogService;
	@Autowired
	HystrixSysHttpLogServiceClient hystrixSysHttpLogServiceClient;
	@Autowired
	IdService IdService;
	@Override
	public void log(HttpServletRequest request, int time) {
		
		try {
			SysHttpLogDto httpLog = new SysHttpLogDto();
			httpLog.setId(IdService.newOne());
            httpLog.setIp(HttpRequestUtils.getIp(request));
            httpLog.setMethod(request.getMethod());
            httpLog.setUrl(request.getRequestURL().toString());
            httpLog.setHeaders(jsonService.toJson(HttpRequestUtils.getHeaderMap(request)));
            httpLog.setParameters(jsonService.toJson(request.getParameterMap()));
            httpLog.setCreateTime(new Date());
            httpLog.setExecuteTime(time);
                         
            hystrixSysHttpLogServiceClient.insert(jsonService.toJson(httpLog));

        } catch (Exception exception) {
            exceptionLogService.log(exception, HttpRequestMeta.createByRequest(request));
        }

	}


}
