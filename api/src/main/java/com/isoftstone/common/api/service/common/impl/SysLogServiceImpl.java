package com.isoftstone.common.api.service.common.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.common.constant.ErrorCodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.isoftstone.common.api.domain.comon.HttpRequestMeta;
import com.isoftstone.common.api.service.cache.CacheService;
import com.isoftstone.common.api.service.cache.LocalCacheFactory;
import com.isoftstone.common.api.service.common.ExceptionLogService;
import com.isoftstone.common.api.service.common.SysLogService;
import com.isoftstone.common.api.service.interceptor.PublicBusinessService;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.api.util.HttpRequestUtils;
import com.isoftstone.common.common.sys.SysUserDto;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;

@Service
public class SysLogServiceImpl implements SysLogService {

	@Value("${cache.type:javaCache}")
	String cacheType;

	@Autowired
	JsonService jsonService;
	@Autowired
	ExceptionLogService exceptionLogService;
	@Autowired
	LocalCacheFactory localCacheFactory;
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;

	@Override
	public void log(HttpServletRequest request) {
		String content = "";
		String username = "";
		String businesscode = "";
		String uri = request.getRequestURI();
		if (uri.contains("/login")) {
			username = request.getParameter("username");
			content = "用户登录";
		} else {
			String token = request.getParameter("token");
			if (StringUtils.isEmpty(token)) {
				token = request.getHeader("token");
			}

			CacheService cacheService = localCacheFactory
					.getCacheService(cacheType);
			SysUserDto sysUserDto = (SysUserDto) cacheService
					.get("user", token);
			if (sysUserDto == null)
				return;
			
			username = sysUserDto.getUsername();
			if (uri.contains("/logout")) {
				content = "用户退出";
			} else {
				businesscode = request.getParameter("businessCode");
				if(businesscode!=null) {
					VisuaSqlExample result = hystrixVisuaSqlExampleClient
							.getByBusinessCode(businesscode);
					content = result.getTitle();
				}
			}
		}
		try {
			Map<String, Object> objMap = new HashMap<String, Object>();
			objMap.put("username", username);
			objMap.put("ip", HttpRequestUtils.getIp(request));
			objMap.put("uri",uri);
			objMap.put("businesscode", businesscode);
			objMap.put("content", content);
			objMap.put("parameters", jsonService.toJson(request.getParameterMap()));
			objMap.put("headers", jsonService.toJson(HttpRequestUtils.getHeaderMap(request)));
			objMap.put("create_date", new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(new Date()));
			objMap.put("data_status", "1");

			/*Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("params", objMap);*/

			String params = jsonService.toJson(objMap);

			Map<String, Object> datas = hystrixVisuaSqlExampleClient
					.getByDataBusinessCode("I09001", params);

			if (datas != null && datas.containsKey(ErrorCodeConstants.HASH_ERR)) {
				exceptionLogService.log(new Exception("写入日志错误"),
						datas.get(ErrorCodeConstants.HASH_ERR).toString());
			}

		} catch (Exception exception) {
			exceptionLogService.log(exception,
					HttpRequestMeta.createByRequest(request));
		}

	}

}
