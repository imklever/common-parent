package com.isoftstone.common.jd;

import com.isoftstone.common.IDEntity;

public class FaceIdCollectionDto extends IDEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 514368944257642980L;

	private String faceId;

    private String gender;

    private String age;

    private String device;

    private Long captureTime;

    private String image;

    private String feature;

    private Integer appearedNum;

    private Integer deviceType;
    
    private String unitId;
    
    private String captureTimestamp;
    
    public String getCaptureTimestamp() {
		return captureTimestamp;
	}

	public void setCaptureTimestamp(String captureTimestamp) {
		this.captureTimestamp = captureTimestamp;
	}

	public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device == null ? null : device.trim();
    }

    public Long getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Long captureTime) {
        this.captureTime = captureTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature == null ? null : feature.trim();
    }

    public Integer getAppearedNum() {
        return appearedNum;
    }

    public void setAppearedNum(Integer appearedNum) {
        this.appearedNum = appearedNum;
    }

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
}