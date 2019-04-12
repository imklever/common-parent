package com.isoftstone.common.common.hystrix;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.PageInfo;
import com.isoftstone.common.common.CommonSqlServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class HystrixCommonSqlServiceClient implements CommonSqlServiceClient{
	@Autowired
	CommonSqlServiceClient commonSqlServiceClient;
	@Override
	@HystrixCommand(fallbackMethod = "getSqlQueryFallBackCall")
	public List<Map<String, Object>> getSqlQuery(String sql, String params,String dataSource) {
		return commonSqlServiceClient.getSqlQuery(sql, params,dataSource);
	}
	public List<Map<String, Object>> getSqlQueryFallBackCall(String sql, String params,String dataSource) {
		return null;
	}

	@Override
	@HystrixCommand(fallbackMethod = "updateSqlFallBackCall")
	public int updateSql(String sql, String params,String dataSource) {
		return commonSqlServiceClient.updateSql(sql, params,dataSource);
	}
	public int updateSqlFallBackCall(String sql, String params,String dataSource) {
		return 0;
	}

	@Override
	@HystrixCommand(fallbackMethod = "addSqlFallBackCall")
	public int addSql(String sql, String params,String dataSource) {
		return commonSqlServiceClient.addSql(sql, params,dataSource);
	}
	public int addSqlFallBackCall(String sql, String params,String dataSource) {
		return 0;
	}

	@Override
	@HystrixCommand(fallbackMethod = "delSqlFallBackCall")
	public int delSql(String sql, String params,String dataSource) {
		return commonSqlServiceClient.delSql(sql, params,dataSource);
	}
	public int delSqlFallBackCall(String sql, String params,String dataSource) {
		return 0;
	}

	@Override
	@HystrixCommand(fallbackMethod = "getSqlQueryByPageFallBackCall")
	public PageInfo<Map<String, Object>> getSqlQueryByPage(String sql, Integer page, Integer size, List<String> sort,
			String params,String dataSource) {
		return commonSqlServiceClient.getSqlQueryByPage(sql, page, size, sort, params,dataSource);
	}
	public PageInfo<Map<String, Object>> getSqlQueryByPageFallBackCall(String sql, Integer page, Integer size, List<String> sort,
			String params,String dataSource) {
		return null;
	}

}
