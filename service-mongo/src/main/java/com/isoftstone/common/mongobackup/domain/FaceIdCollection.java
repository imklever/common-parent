package com.isoftstone.common.mongobackup.domain;

public class FaceIdCollection {
	
	private String id;
	private String face_id;
	private String gender;
	private String age;
	private String device;
	private long capture_time;
	private String image;
	private String feature;
	private int appeared_num;
	
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public long getCapture_time() {
		return capture_time;
	}
	public void setCapture_time(long capture_time) {
		this.capture_time = capture_time;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public int getAppeared_num() {
		return appeared_num;
	}
	public void setAppeared_num(int appeared_num) {
		this.appeared_num = appeared_num;
	}
}
