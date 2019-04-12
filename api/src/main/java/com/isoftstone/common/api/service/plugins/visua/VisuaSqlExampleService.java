package com.isoftstone.common.api.service.plugins.visua;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.isoftstone.common.plugins.visua.VisuaSqlExample;


public interface VisuaSqlExampleService {

	Object testMode(String dtoJson, String name, String type) throws Exception;
	void exportExcel(HttpServletResponse res, String params, VisuaSqlExample result, List<String> sort);

}
