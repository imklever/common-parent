package com.isoftstone.common.jd;

import com.isoftstone.common.IDEntity;

public class FaceVisitorLogDto extends IDEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8245104181873642506L;

	private String unitId;

    private String faceId;

    private String faceDate;

    private String dateTime;

    private String image;

    private Integer deviceType;

    private String device;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId == null ? null : unitId.trim();
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getFaceDate() {
        return faceDate;
    }

    public void setFaceDate(String faceDate) {
        this.faceDate = faceDate == null ? null : faceDate.trim();
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime == null ? null : dateTime.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
}