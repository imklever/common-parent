package com.isoftstone.common.action.impl;



import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.isoftstone.common.action.BaseVisuaActionService;
import com.isoftstone.common.plugins.visua.vo.VisuaContextVo;
import com.isoftstone.common.service.CommonSqlService;
import com.isoftstone.common.util.JsonService;

@Service
public class SelSqlVisuaActionServiceImpl extends BaseVisuaActionService{
	public int isReturn;
	public String sql;
	@Autowired
    CommonSqlService commonSqlService;
	@Autowired
	private JsonService jsonService;

	@Override
	public String subExecute(VisuaContextVo parms) {
		if(!StringUtils.isEmpty(sql)) {
			List<Map<String, Object>> list =commonSqlService.getSqlQuery(sql, "{}", null);
			System.out.println(jsonService.toJson(list));
			if(isReturn==1) {
				System.out.println("返回数据");
			}
		}
		return "0";
	}
 
}
