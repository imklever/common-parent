package com.isoftstone.common.plugins.visua;

import java.util.List;
import java.util.Map;

import org.common.constant.PageConstants;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.isoftstone.common.PageInfo;

public interface VisuaSqlExampleDefinition {
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	VisuaSqlExample save(@RequestBody String dtoJson);
	
	@RequestMapping(value = "/getByBusinessCode", method = RequestMethod.GET)
	VisuaSqlExample getByBusinessCode(@RequestParam("businessCode") String businessCode);
		
	@RequestMapping(value = "/getByBusinessCodeCount", method = RequestMethod.GET)
	int getByBusinessCodeCount(@RequestParam("businessCode") String businessCode);
	
	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	VisuaSqlExample findById(@RequestParam("id") String id);
	
	@RequestMapping(value = "/getByDataBusinessCode", method = RequestMethod.POST)
	Map<String,Object> getByDataBusinessCode(@RequestParam("businessCode")String businessCode,
			@RequestParam("params")String params);
	
	@RequestMapping(value = "/getDataByOneToMany", method = RequestMethod.POST)
	Map<String,Object> getDataByOneToMany(@RequestParam("businessCode")String businessCode,
			@RequestParam("params")String params);
	
	@RequestMapping(value = "/getByPage", method = RequestMethod.POST)
	PageInfo<VisuaSqlExample> getByPage(@RequestParam(value = "visuaSqlExample", required = false, defaultValue = "{}") String visuaSqlExample,
    		@RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer size,
			@RequestParam(value = "sort", required = false) List<String> sort);
	
	@RequestMapping(value = "/getDataByPage", method = RequestMethod.POST)
	PageInfo<Map<String, Object>> getDataByPage(
			@RequestParam("businessCode")String businesscode, 
			@RequestParam(value = "params", required = false, defaultValue = "{}")String params, 
			@RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer size,
			@RequestParam(value = "sort", required = false) List<String> sort);

	@RequestMapping(value = "/findByUserId", method = RequestMethod.POST)
	List<VisuaSqlExample> findByUserId(@RequestParam("userId") String userId);
	
	@RequestMapping(value = "/testMode", method = RequestMethod.POST)
	Map<String, Object> testMode(@RequestParam("dtoJson")String dtoJson, 
					@RequestParam("name")String name,
					@RequestParam("type") String type);
	
	@RequestMapping(value = "/getDataByOneToManyByPage", method = RequestMethod.POST)
	PageInfo<Map<String, Object>> getDataByOneToManyByPage(@RequestParam("businessCode")String businessCode,
				@RequestParam("params")String params, @RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer page,
				@RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer size,
				@RequestParam(value = "sort", required = false) List<String> sort);
}
