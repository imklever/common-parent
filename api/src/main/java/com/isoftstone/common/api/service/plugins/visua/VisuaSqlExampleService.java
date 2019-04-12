package com.isoftstone.common.api.service.plugins.visua;

import com.isoftstone.common.plugins.visua.VisuaSqlExample;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface VisuaSqlExampleService {

	Object testMode(String dtoJson, String name, String type) throws Exception;
	void exportExcel(HttpServletResponse res, String params, VisuaSqlExample result, List<String> sort);

}
