package com.isoftstone.common.action.impl;


import org.springframework.stereotype.Service;

import com.isoftstone.common.action.BaseVisuaActionService;
import com.isoftstone.common.plugins.visua.vo.VisuaContextVo;
@Service
public class StartVisuaActionServiceImpl extends BaseVisuaActionService{
	
	@Override
	public String subExecute(VisuaContextVo parms) {
		System.out.println("开始");
		return "0";
	}

}
