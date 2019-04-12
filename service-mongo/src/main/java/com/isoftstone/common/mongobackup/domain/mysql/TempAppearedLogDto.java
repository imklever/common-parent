package com.isoftstone.common.mongobackup.domain.mysql;

import java.math.BigDecimal;

public class TempAppearedLogDto{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6750822826986934515L;

	private String faceId;

    private String deviceType;

    private Long maxAppearedTime;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    public Long getMaxAppearedTime() {
        return maxAppearedTime;
    }

    public void setMaxAppearedTime(Long maxAppearedTime) {
        this.maxAppearedTime = maxAppearedTime;
    }
}