package com.isoftstone.common.common;
/**
 * 上传文件的基本属性
 * @author cpniuc
 *
 */
public class FileAttrDto {
	private String id;
	private String fileType;      //类型
	private String filePath;      //访问路径
	private int fileSize;         //文件大小
	private String savePath;      //保存路径本地绝对路径
	private String relativePath;  //文件相对路径
	private String md5;           //文件md5值
	private String name;          //文件名称
	private String imgPath;       //图片路径
	private int    audioTime;     //播放时长
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public int getAudioTime() {
		return audioTime;
	}
	public void setAudioTime(int audioTime) {
		this.audioTime = audioTime;
	}
	
	
}
