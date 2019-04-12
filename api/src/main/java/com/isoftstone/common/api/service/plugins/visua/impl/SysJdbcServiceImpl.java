package com.isoftstone.common.api.service.plugins.visua.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.api.service.plugins.visua.SysJdbcService;
import com.isoftstone.common.common.sys.SysJdbcDto;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.ServiceException;
@Service
public class SysJdbcServiceImpl implements SysJdbcService{
	@Autowired
	JsonService jsonService;

	@Override
	public boolean verifyConnection(String dtoJson) {
		SysJdbcDto sysJdbcDto =jsonService.parseObject(dtoJson, SysJdbcDto.class);
		try {
			Class.forName(sysJdbcDto.getDriverName());
			Connection  con=DriverManager.getConnection(sysJdbcDto.getJdbcUrl(),
					sysJdbcDto.getName(), sysJdbcDto.getPassWord());
			DatabaseMetaData databaseMetaData=con.getMetaData();
			con.close();
		} catch (SQLException e) {
			throw new ServiceException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new ServiceException(e.getMessage());
		}
		return true;
	}
}
