package com.isoftstone.common.plugins.visua.vo;

public class VisuaSqlVo {
	private String name;
	private String desc;
	private String value;
	private String type;
	private Integer isReturn;
	private String dataSource;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getIsReturn() {
		return isReturn;
	}
	public void setIsReturn(Integer isReturn) {
		this.isReturn = isReturn;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getDesc() {
		return desc==null?"":desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
