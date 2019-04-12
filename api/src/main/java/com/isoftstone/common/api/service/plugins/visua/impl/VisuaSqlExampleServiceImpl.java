package com.isoftstone.common.api.service.plugins.visua.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.isoftstone.common.PageInfo;
import com.isoftstone.common.api.service.plugins.visua.VisuaSqlExampleService;
import com.isoftstone.common.api.util.bean.ExportExcel;
import com.isoftstone.common.common.hystrix.HystrixCommonSqlServiceClient;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.plugins.visua.vo.VisuaSqlVo;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.SqlTemplateReplacement;
import org.common.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisuaSqlExampleServiceImpl implements VisuaSqlExampleService {
	@Autowired
	SqlTemplateReplacement sqlTemplateReplacement;
	@Autowired
	JsonService jsonService;
	@Autowired
	HystrixCommonSqlServiceClient hystrixCommonSqlServiceClient;
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;

	/**
	 * 分模板测试
	 * @throws Exception 
	 */
	@Override
	public Object testMode(String dtoJson, String name, String type) throws Exception {
		VisuaSqlExample visuaSqlExample = jsonService.parseObject(dtoJson, VisuaSqlExample.class);
		String sqlTemplates = visuaSqlExample.getSqlTemplates();
		List<VisuaSqlVo> visuaSqlVos = jsonService.parseArray(sqlTemplates, VisuaSqlVo.class);
		Map<String, Object> paramsMap = new HashMap<>();
		sqlTemplateReplacement.createReplaceTestMap(paramsMap, visuaSqlExample.getInputData());
		String params=sqlTemplateReplacement.createReplaceTestStr(visuaSqlExample.getInputData());
		for (VisuaSqlVo visuaSqlVo : visuaSqlVos) {
			String sql= sqlTemplateReplacement.templateReplacement(paramsMap, visuaSqlVo.getValue());
			if (name.equals(visuaSqlVo.getName())) {
				if (CommonConstants.TEST_MODE_TYPE_SQL.equals(type)) {
					return sql;
				} else if (CommonConstants.TEST_MODE_TYPE_EXECSQL.equals(type)) {
					sqlRun(paramsMap, sql, visuaSqlVo,params);
					return paramsMap.get(name);
				}
			}
			sqlRun(paramsMap, sql, visuaSqlVo,params);
		}
		return null;
	}

	private void sqlRun(Map<String, Object> paramsMap, String sql, VisuaSqlVo visuaSqlVo,String params) {
		
		if (visuaSqlVo.getType() == null || CommonConstants.VISUA_TYPE_SEL.equals(visuaSqlVo.getType())) {
			List<Map<String, Object>> list = hystrixCommonSqlServiceClient.getSqlQuery(sql,
					params,visuaSqlVo.getDataSource());
			paramsMap.put(visuaSqlVo.getName(), list);
		} else if (CommonConstants.VISUA_TYPE_ADD.equals(visuaSqlVo.getType())) {
			int count = hystrixCommonSqlServiceClient.addSql(sql,params
					,visuaSqlVo.getDataSource());
			paramsMap.put(visuaSqlVo.getName(), count);
		} else if (CommonConstants.VISUA_TYPE_DEL.equals(visuaSqlVo.getType())) {
			int count = hystrixCommonSqlServiceClient.delSql(sql,params
					,visuaSqlVo.getDataSource());
			paramsMap.put(visuaSqlVo.getName(), count);
		} else if (CommonConstants.VISUA_TYPE_EDIT.equals(visuaSqlVo.getType())) {
			int count = hystrixCommonSqlServiceClient.updateSql(sql, params
					,visuaSqlVo.getDataSource());
			paramsMap.put(visuaSqlVo.getName(), count);
		}/*else if(CommonConstants.VISUA_TYPE_CHECK.equals(visuaSqlVo.getType())) {
			paramsMap.put(visuaSqlVo.getName(), sql);
		}*/
	}
/**
 * Excel文件导出
 */
	@Override
	public void exportExcel(HttpServletResponse res, String params, VisuaSqlExample result, List<String> sort) {
		int pageSize=10000;
		String sql =null;
		String sqlTemplates = result.getSqlTemplates();
		List<VisuaSqlVo> visuaSqlVos = jsonService.parseArray(sqlTemplates, VisuaSqlVo.class);
		for (VisuaSqlVo visuaSqlVo : visuaSqlVos) {
			if(visuaSqlVo.getIsReturn()==1) {
				 sql = visuaSqlVo.getValue();
			}
		}
		PageInfo<Map<String, Object>> dataset2 = hystrixVisuaSqlExampleClient.getDataByPage(result.getBusinessCode(), params, 1, pageSize, sort);
		List<Map<String, Object>>dataset=dataset2.getList();
		for (int pageNum=2;pageNum<=dataset2.getPages();pageNum++) {
			    PageInfo<Map<String, Object>> paneg = hystrixVisuaSqlExampleClient.getDataByPage(result.getBusinessCode(), params, pageNum, pageSize, sort);
				try {
					dataset.addAll(paneg.getList());
				} catch (Exception e) {
					 paneg=hystrixVisuaSqlExampleClient.getDataByPage(result.getBusinessCode(), params, pageNum, pageSize, sort);
					 try {
						 dataset.addAll(paneg.getList());
					} catch (Exception e2) {
					}
				}
		}
		res.setHeader("content-Type", "application/vnd.ms-excel");
		   // 下载文件的默认名称
			try {
				res.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(result.getTitle()+".xls", "utf-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		JSONArray array=JSONObject.parseArray(result.getOutputData());
		String[] headers=new String[array.size()];
		String[] fields=new String[array.size()] ;
		for (int i = 0; i < array.size(); i++) {
			JSONObject json=JSONObject.parseObject(array.get(i).toString());
			fields[i]=(String) json.get("name");
			headers[i]=(String) json.get("value");
		}
		ExportExcel<Map<String, Object>> exportExcel=new ExportExcel<Map<String,Object>>();
		try {
			exportExcel.exportExcel(result.getTitle(), headers, fields, dataset, res.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
}
