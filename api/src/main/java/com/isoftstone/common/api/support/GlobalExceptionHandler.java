package com.isoftstone.common.api.support;

import com.google.common.collect.Maps;
import com.isoftstone.common.api.domain.comon.HttpRequestMeta;
import com.isoftstone.common.api.service.common.ExceptionLogService;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Configuration
@ControllerAdvice(annotations = { RestController.class })
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@Autowired
	private JsonService jsonService;

	@Autowired
	private ExceptionLogService exceptionLogService;

	/**
	 * 重载ResponseEntityExceptionHandler的方法，加入日志
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		logError(ex);

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
		}

		APIResult result = APIResult.createInstance();

		if (ex instanceof ServiceException) {
			final ServiceException serviceException = (ServiceException) ex;
			result.setCode(serviceException.errorCode.code);
			result.setMessage(serviceException.getMessage());
		} else {
			result.setCode(0);
			result.setMessage(ex.getMessage());
		}

		return new ResponseEntity<Object>(result, headers, status);
	}

	public void logError(Exception ex) {

		exceptionLogService.log(ex);

		Map<String, String> map = Maps.newHashMap();
		map.put("message", ex.getMessage());
		logger.error(jsonService.toJson(map), ex);
	}

	public void logError(Exception ex, HttpServletRequest request) {

		exceptionLogService.log(ex, HttpRequestMeta.createByRequest(request));

		Map<String, String> map = Maps.newHashMap();
		map.put("message", ex.getMessage());
		map.put("from", request.getRemoteAddr());
		String queryString = request.getQueryString();
		map.put("path", queryString != null ? (request.getRequestURI() + "?" + queryString) : request.getRequestURI());

		logger.error(jsonService.toJson(map), ex);
	}

}
