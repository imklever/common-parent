package com.isoftstone.common.plugins.visua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.isoftstone.common.IDEntity;
import java.util.Date;

public class VisuaSqlExample extends IDEntity {
	private static final long serialVersionUID = 1L;

	private String title;

    private String businessCode;

    private String createUser;

    private Integer sqlStatus;
    
    private String supportToDo;
    
    private String udfToDo;
    
    
    private Integer versionNo;

    private Integer businessType;

    private Integer sqlType;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    private String outputData;

    private String inputData;

    private String sqlTemplates;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode == null ? null : businessCode.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Integer getSqlStatus() {
        return sqlStatus;
    }

    public void setSqlStatus(Integer sqlStatus) {
        this.sqlStatus = sqlStatus;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getSqlType() {
        return sqlType;
    }

    public void setSqlType(Integer sqlType) {
        this.sqlType = sqlType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getOutputData() {
        return outputData;
    }

    public void setOutputData(String outputData) {
        this.outputData = outputData == null ? null : outputData.trim();
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData == null ? null : inputData.trim();
    }

    public String getSqlTemplates() {
        return sqlTemplates;
    }

    public void setSqlTemplates(String sqlTemplates) {
        this.sqlTemplates = sqlTemplates == null ? null : sqlTemplates.trim();
    }

	public String getSupportToDo() {
		return supportToDo;
	}

	public void setSupportToDo(String supportToDo) {
		this.supportToDo = supportToDo;
	}

	public String getUdfToDo() {
		return udfToDo;
	}

	public void setUdfToDo(String udfToDo) {
		this.udfToDo = udfToDo;
	}

    public Integer getVersionNo() {
        return versionNo==null?1:versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }
    
    
}