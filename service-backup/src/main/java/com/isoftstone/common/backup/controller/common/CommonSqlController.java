package com.isoftstone.common.backup.controller.common;

import java.util.List;
import java.util.Map;

import org.common.constant.PageConstants;
import org.common.constant.ServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.PageInfo;
import com.isoftstone.common.common.CommonSqlServiceDefinition;
import com.isoftstone.common.service.CommonSqlService;
@RefreshScope
@RestController
@RequestMapping(name = ServiceBackConstants.BACK_SERVICE,
 	path = ServiceBackConstants.PATH_COMMON_SQL)
public class CommonSqlController implements CommonSqlServiceDefinition{
	@Autowired
	CommonSqlService commonSqlService;
	@Override
	public List<Map<String, Object>> getSqlQuery(
			@RequestParam(value = "sql", required = true)String sql,
			@RequestParam(value = "params", required = false,defaultValue = "{}")String params,
			@RequestParam(value = "dataSource", required = false, defaultValue = "")String dataSource) {
		List<Map<String, Object>> list=commonSqlService.getSqlQuery(sql,params,dataSource);
		return list;
	}

	@Override
	public int updateSql(@RequestParam(value = "sql", required = true)String sql,
			@RequestParam(value = "params", required = false,defaultValue = "{}")String params,
			@RequestParam(value = "dataSource", required = false, defaultValue = "")String dataSource) {
		return commonSqlService.updateSql(sql,params,dataSource);
	}

	@Override
	public int addSql(@RequestParam(value = "sql", required = true)String sql,
			@RequestParam(value = "params", required = false,defaultValue = "{}")String params,
			@RequestParam(value = "dataSource", required = false, defaultValue = "")String dataSource) {
		return commonSqlService.addSql(sql,params,dataSource);
	}

	@Override
	public int delSql(@RequestParam(value = "sql", required = true)String sql,
			@RequestParam(value = "params", required = false,defaultValue = "{}")String params,
			@RequestParam(value = "dataSource", required = false, defaultValue = "")String dataSource) {
		return commonSqlService.delSql(sql,params,dataSource);
	}

	@Override
	public PageInfo<Map<String, Object>> getSqlQueryByPage(@RequestParam(value = "sql", required = true)String sql,
			@RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "") List<String> sort,
            @RequestParam(value = "params", required = false,defaultValue = "{}")String params,
            @RequestParam(value = "dataSource", required = false, defaultValue = "")String dataSource) {
		return  commonSqlService.getSqlQueryByPage(sql,page,size,sort,params,dataSource);
	}

}
