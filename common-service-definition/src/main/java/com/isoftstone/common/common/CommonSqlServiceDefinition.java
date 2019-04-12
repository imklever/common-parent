package com.isoftstone.common.common;

import java.util.List;
import java.util.Map;

import org.common.constant.PageConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.isoftstone.common.PageInfo;


public interface CommonSqlServiceDefinition {
	@RequestMapping(value = "/getSqlQuery", method = RequestMethod.GET)
	List<Map<String, Object>>  getSqlQuery(
			@RequestParam(value = "sql", required = true)String sql,
			@RequestParam(value = "params", required = false,defaultValue = "{}")String params,
			@RequestParam(value = "dataSource", required = false, defaultValue = "")String dataSource
			);
	@RequestMapping(value = "/updateSql", method = RequestMethod.GET)
	int  updateSql(@RequestParam(value = "sql", required = true)String sql,
			@RequestParam(value = "params", required = false,defaultValue = "{}")String params,
			@RequestParam(value = "dataSource", required = false, defaultValue = "")String dataSource
			);
	
	@RequestMapping(value = "/addSql", method = RequestMethod.GET)
	int  addSql(@RequestParam(value = "sql", required = true)String sql,
			@RequestParam(value = "params", required = false,defaultValue = "{}")String params,
			@RequestParam(value = "dataSource", required = false, defaultValue = "")String dataSource
			);
	@RequestMapping(value = "/delSql", method = RequestMethod.GET)
	int  delSql(@RequestParam(value = "sql", required = true)String sql,
			@RequestParam(value = "params", required = false,defaultValue = "{}")String params,
			@RequestParam(value = "dataSource", required = false, defaultValue = "")String dataSource
			);
	
	@RequestMapping(value = "/getSqlQueryByPage", method = RequestMethod.GET)
	public PageInfo<Map<String, Object>> getSqlQueryByPage(
			@RequestParam(value = "sql", required = true)String sql, 
			@RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "") List<String> sort,
            @RequestParam(value = "params", required = false,defaultValue = "{}")String params,
            @RequestParam(value = "dataSource", required = false, defaultValue = "")String dataSource);
	
	
}
