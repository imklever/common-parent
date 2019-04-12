package com.isoftstone.common.common.sys;

import com.isoftstone.common.IDEntity;

public class SysUserDto extends IDEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3659941484610805747L;

	private String username;

    private String password;

    private String token;

    private String updatetime;

    private String overduetime;

    private String createUser;

    private String createDate;

    private String dataStatus;

    private String unitId;

    private String phone;
    
    private String roleList;
    
    private int sysAdmin; 
    
    private int sysType;
    
    private String isDefaultPwd;
    
    public String getIsDefaultPwd() {
		return isDefaultPwd;
	}

	public void setIsDefaultPwd(String isDefaultPwd) {
		this.isDefaultPwd = isDefaultPwd;
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public int getSysType() {
        return sysType;
    }

    public void setSysType(int sysType) {
        this.sysType = sysType;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getOverduetime() {
        return overduetime;
    }

    public void setOverduetime(String overduetime) {
        this.overduetime = overduetime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus == null ? null : dataStatus.trim();
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getRoleList() {
        return roleList;
    }

    public void setRoleList(String roleList) {
        this.roleList = roleList == null ? null : roleList.trim();
    }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSysAdmin() {
		return sysAdmin;
	}

	public void setSysAdmin(int sysAdmin) {
		this.sysAdmin = sysAdmin;
	}
	
	
}