package com.isoftstone.common.plugins.visua.vo;


public class  VisuaActionVo {
	private String objClass;
	private String name;
	private String id;
	private String params;
	private int result;
	public String getObjClass() {
		return objClass;
	}
	public void setObjClass(String objClass) {
		this.objClass = objClass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public int getResult() {
		return result;
	}
	public synchronized void setResult(int result) {
		this.result = result;
	}
	
	
	
}
