package com.isoftstone.common.mongobackup.domain.mysql;

public class FaceIdTypeCountDto {
	
	private String faceType;
	private int faceCount;
	
	public String getFaceType() {
		return faceType;
	}
	public void setFaceType(String faceType) {
		this.faceType = faceType;
	}
	public int getFaceCount() {
		return faceCount;
	}
	public void setFaceCount(int faceCount) {
		this.faceCount = faceCount;
	}
}
