package com.isoftstone.common.mongobackup.domain;

public class LogCollection {

	private String id;
	private String face_id; 
	private String device;
	private long appeared_time;
	private String image;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFace_id() {
		return face_id;
	}
	public void setFace_id(String face_id) {
		this.face_id = face_id;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public long getAppeared_time() {
		return appeared_time;
	}
	public void setAppeared_time(long appeared_time) {
		this.appeared_time = appeared_time;
	}

}
