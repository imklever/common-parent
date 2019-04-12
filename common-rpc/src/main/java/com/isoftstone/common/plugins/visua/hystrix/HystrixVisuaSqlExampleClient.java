package com.isoftstone.common.plugins.visua.hystrix;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.PageInfo;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.plugins.visua.VisuaSqlExampleClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class HystrixVisuaSqlExampleClient implements VisuaSqlExampleClient{
	@Autowired
	VisuaSqlExampleClient visuaSqlExampleClient;
	@Override
	@HystrixCommand(fallbackMethod = "saveFallBackCall")
	public VisuaSqlExample save(String dtoJson) {
		return visuaSqlExampleClient.save(dtoJson);
	}
	public VisuaSqlExample saveFallBackCall(String dtoJson) {
		return null;
	}

	@Override
	@HystrixCommand(fallbackMethod = "getByBusinessCodeFallBackCall")
	public VisuaSqlExample getByBusinessCode(String businesscode) {
		return visuaSqlExampleClient.getByBusinessCode(businesscode);
	}
	public VisuaSqlExample getByBusinessCodeFallBackCall(String businesscode) {
		return null;
	}

	@Override
	@HystrixCommand(fallbackMethod = "findByIdFallBackCall")
	public VisuaSqlExample findById(String id) {
		return visuaSqlExampleClient.findById(id);
	}
	public VisuaSqlExample findByIdFallBackCall(String id) {
		return null;
	}

	@Override
	@HystrixCommand(fallbackMethod = "getByDataBusinessCodeFallBackCall")
	public Map<String, Object> getByDataBusinessCode(String businesscode, String params) {
		return visuaSqlExampleClient.getByDataBusinessCode(businesscode, params);
	}
	
	public Map<String, Object> getByDataBusinessCodeFallBackCall(String businesscode, String params) {
		return null;
	}
	@HystrixCommand(fallbackMethod = "getByBusinessCodeCountFallBackCall")
	public int getByBusinessCodeCount(String businessCode) {
		return visuaSqlExampleClient.getByBusinessCodeCount(businessCode);
	}
	public int getByBusinessCodeCountFallBackCall(String businessCode) {
		return 10;
	}
	@HystrixCommand(fallbackMethod = "getByPageFallBackCall")
	public PageInfo<VisuaSqlExample> getByPage(String visuaSqlExample, Integer page, Integer size, List<String> sort) {
		return visuaSqlExampleClient.getByPage( visuaSqlExample,  page,  size,  sort);
	}
	public PageInfo<VisuaSqlExample> getByPageFallBackCall(String visuaSqlExample, Integer page, Integer size, List<String> sort) {
		return null;
	}
	@HystrixCommand(fallbackMethod = "getDataByPageFallBackCall")
	public PageInfo<Map<String, Object>> getDataByPage(String businesscode, String params, Integer page, Integer size,
			List<String> sort) {
		return visuaSqlExampleClient.getDataByPage(businesscode,  params,  page,  size,sort);
	}
	public PageInfo<Map<String, Object>> getDataByPageFallBackCall(String businesscode, String params, Integer page, Integer size,
			List<String> sort) {
		return null;
	}
	@Override
	@HystrixCommand(fallbackMethod = "getDataByOneToManyFallBackCall")
	public Map<String, Object> getDataByOneToMany(String businessCode, String params) {
		return visuaSqlExampleClient.getDataByOneToMany(businessCode, params);
	}
	public Map<String, Object> getDataByOneToManyFallBackCall(String businessCode, String params) {
		return null;
	}
	@Override
	@HystrixCommand(fallbackMethod = "findByUserIdFallBackCall")
	public List<VisuaSqlExample> findByUserId(String userId) {
		return visuaSqlExampleClient.findByUserId(userId);
	}
	public  List<VisuaSqlExample> findByUserIdFallBackCall(String userId) {
		return null;
	}
	@HystrixCommand(fallbackMethod = "getDataByOneToManyByPageFallBackCall")
	public PageInfo<Map<String, Object>> getDataByOneToManyByPage(String businesscode, String params, Integer page, Integer size,
			List<String> sort) {
		return visuaSqlExampleClient.getDataByOneToManyByPage(businesscode, params, page, size, sort);
	}
	public PageInfo<Map<String, Object>> getDataByOneToManyByPageFallBackCall(String businesscode, String params, Integer page, Integer size,
			List<String> sort) {
		return null;
	}
	@HystrixCommand(fallbackMethod = "testModeFallBackCall")
	public Map<String, Object> testMode(String dtoJson, String name, String type) {
		return visuaSqlExampleClient.testMode(dtoJson,name,type);
	}
	public Map<String, Object> testModeFallBackCall(String dtoJson, String name, String type) {
		return null;
	}

}
