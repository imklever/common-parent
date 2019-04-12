package com.isoftstone.common.action;


import com.isoftstone.common.plugins.visua.vo.VisuaContextVo;

public abstract class BaseVisuaActionService implements VisuaActionService{
	public String result="-1";//代表未运行
	public String name;//代表未运行
	public boolean start;
	public boolean end;

	@Override
	public String getResult() {
		return result;
	}
	public	 abstract String subExecute(VisuaContextVo parms);
	
	@Override
	public String execute(VisuaContextVo parms) {
		try {
			result=subExecute(parms);
		} catch (Exception e) {
			result="1";
		}finally {
			
		}
		return result;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isStart() {
		return start;
	}

	@Override
	public boolean isEnd() {
		return end;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setStart(boolean start) {
		this.start = start;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}
	
	
	
}
