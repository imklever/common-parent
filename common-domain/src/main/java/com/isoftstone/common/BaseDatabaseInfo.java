package com.isoftstone.common;

public class BaseDatabaseInfo {
	
	private String id;
	private String dataBaseType;
	private String driver;
	private String url;
	private String username;
	private String password;
	private String yjSql;
	private String sjSql;
	private String gtSql;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDataBaseType() {
		return dataBaseType;
	}
	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}
	public String getYjSql() {
		return yjSql;
	}
	public void setYjSql(String yjSql) {
		this.yjSql = yjSql;
	}
	public String getSjSql() {
		return sjSql;
	}
	public void setSjSql(String sjSql) {
		this.sjSql = sjSql;
	}
	public String getGtSql() {
		return gtSql;
	}
	public void setGtSql(String gtSql) {
		this.gtSql = gtSql;
	}
}
