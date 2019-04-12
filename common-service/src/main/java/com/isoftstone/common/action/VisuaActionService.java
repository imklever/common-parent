package com.isoftstone.common.action;


import com.isoftstone.common.plugins.visua.vo.VisuaContextVo;

public interface VisuaActionService {
	
	 String getResult();
	 String execute(VisuaContextVo parms);
	 String getName();
	 boolean isStart();
	 boolean isEnd();
}
