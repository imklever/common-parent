package com.isoftstone.common.backup.service.plugins.visua;

import java.util.List;
import java.util.Map;

import com.isoftstone.common.PageInfo;
import com.isoftstone.common.backup.service.BaseService;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;

public interface VisuaSqlExampleService extends BaseService<VisuaSqlExample, String> {

	VisuaSqlExample getByBusinessCode(String businesscode);

	int getByBusinessCodeCount(String businessCode);

	Map<String, Object> getByDataBusinessCode(String businesscode, String params);

	List<VisuaSqlExample> findBySel(String visuaSqlExample, List<String> sort);

	PageInfo<Map<String, Object>> findBusinessCodeDataByPage(String businesscode, String params, Integer pageNo,
			Integer pageSize, List<String> sort);

	PageInfo<VisuaSqlExample> findByPage(String visuaSqlExample, Integer pageNo, Integer pageSize, List<String> sort);

	Map<String, Object> getDataByOneToMany(String businessCode, String params);

	List<VisuaSqlExample> findByUserId(String userId);

	PageInfo<Map<String, Object>> getDataByOneToManyByPage(String businessCode, String params, Integer page, Integer size,
			List<String> sort);

	Map<String, Object> testMode(String dtoJson, String name, String type);
}
