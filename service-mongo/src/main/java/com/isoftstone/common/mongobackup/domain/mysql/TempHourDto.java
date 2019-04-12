package com.isoftstone.common.mongobackup.domain.mysql;
 

public class TempHourDto {
	
	private String appearedDate;
	private String appearedHour;
	private String gender;
	private int faceCount;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getFaceCount() {
		return faceCount;
	}
	public void setFaceCount(int faceCount) {
		this.faceCount = faceCount;
	}
}