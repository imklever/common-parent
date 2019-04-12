package com.isoftstone.common.plugins.visua.vo;

import java.util.List;
import java.util.Map;

public class VisuaSqlInputDataVo {
	private boolean isSkip;
	private String name;
	private String value;
	private String testVal;
	private List<Map<String,Object>> verifyType;
	private List<String> verifyTypeStr;
	private String dataType; //参数类型--输入参数类型
	public boolean isSkip() {
		return isSkip;
	}
	public void setSkip(boolean isSkip) {
		this.isSkip = isSkip;
	}
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
	public String getTestVal() {
		return testVal;
	}
	public void setTestVal(String testVal) {
		this.testVal = testVal;
	}
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public List<Map<String, Object>> getVerifyType() {
        return verifyType;
    }
    public void setVerifyType(List<Map<String, Object>> verifyType) {
        this.verifyType = verifyType;
    }
    public List<String> getVerifyTypeStr() {
        return verifyTypeStr;
    }
    public void setVerifyTypeStr(List<String> verifyTypeStr) {
        this.verifyTypeStr = verifyTypeStr;
    }
   
	
}
