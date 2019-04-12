package com.isoftstone.common.jd;

import com.isoftstone.common.IDEntity;

public class FaceLogCollectionDto extends IDEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1746664913945634247L;

	private String faceId;

    private String device;

    private Long appearedTime;

    private String image;
    
    private Integer deviceType;

    private String unitId;
    
	private String appearedFormatTime;
	
	private String appearedDate;
	
	private String appearedHour;
	
	private String appearedMinute;
    
    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device == null ? null : device.trim();
    }

    public Long getAppearedTime() {
        return appearedTime;
    }

    public void setAppearedTime(Long appearedTime) {
        this.appearedTime = appearedTime;
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

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAppearedFormatTime() {
		return appearedFormatTime;
	}

	public void setAppearedFormatTime(String appearedFormatTime) {
		this.appearedFormatTime = appearedFormatTime;
	}

	public String getAppearedDate() {
		return appearedDate;
	}

	public void setAppearedDate(String appearedDate) {
		this.appearedDate = appearedDate;
	}

	public String getAppearedHour() {
		return appearedHour;
	}

	public void setAppearedHour(String appearedHour) {
		this.appearedHour = appearedHour;
	}

	public String getAppearedMinute() {
		return appearedMinute;
	}

	public void setAppearedMinute(String appearedMinute) {
		this.appearedMinute = appearedMinute;
	}

	 

}