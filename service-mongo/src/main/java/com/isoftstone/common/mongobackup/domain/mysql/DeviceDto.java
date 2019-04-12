package com.isoftstone.common.mongobackup.domain.mysql;

import com.isoftstone.common.IDEntity;

public class DeviceDto extends IDEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7426380240817402761L;
 
    private String name;

    private String mac;

    private String remark;

    private String typeid;

    private String createTime;
    
    private Integer typeState;
    
    private String storeId;
 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid == null ? null : typeid.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

	public Integer getTypeState() {
		return typeState;
	}

	public void setTypeState(Integer typeState) {
		this.typeState = typeState;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

}