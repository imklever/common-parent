package com.isoftstone.common.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component  
@ConfigurationProperties(prefix="ioc")
//接收application.yml中的ioc下面的属性
public class MyProps {
	private boolean local;
	private String upFilePath;
	private String downFilePath;
	private String exportWordPath;
	private String quartzProper;
	private String driverClassName;
	private String jdbcUrl;
	private String username;
	private String password;
	
	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public String getUpFilePath() {
		return upFilePath;
	}

	public void setUpFilePath(String upFilePath) {
		this.upFilePath = upFilePath;
	}

	public String getDownFilePath() {
		return downFilePath;
	}

	public void setDownFilePath(String downFilePath) {
		this.downFilePath = downFilePath;
	}

	public String getExportWordPath() {
		return exportWordPath;
	}

	public void setExportWordPath(String exportWordPath) {
		this.exportWordPath = exportWordPath;
	}

    public String getQuartzProper() {
        return quartzProper;
    }

    public void setQuartzProper(String quartzProper) {
        this.quartzProper = quartzProper;
    }

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
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
    
}
