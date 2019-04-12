package com.isoftstone.common.backup.controller.common.sys;

import org.common.constant.ServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.backup.service.common.sys.SysUserDtoService;
import com.isoftstone.common.common.sys.SysUserDto;
import com.isoftstone.common.common.sys.SysUserServiceDefinition;
@RefreshScope
@RestController
@RequestMapping(name = ServiceBackConstants.BACK_SERVICE,
 	path = ServiceBackConstants.PATH_SYS_USER)
public class SysUserController implements SysUserServiceDefinition{
	@Autowired
	SysUserDtoService sysUserDtoService;

	@Override
	public SysUserDto findByUserPass(@RequestParam(value = "name", required = true)String name, @RequestParam(value = "password", required = true)String password) {
		return sysUserDtoService.findByNamePass(name, password);
	}

	@Override
	public int updateByUserToken(String name, String token, String overduetime) {
		return sysUserDtoService.updateByUserToken(name, token, overduetime);		
	}

	@Override
	public SysUserDto findById(String id) {
		return sysUserDtoService.findById(id);
	}

	@Override
	public int updateTokenById(String id,String token,String overduetime) {
		return sysUserDtoService.updateTokenById(id,token,overduetime);
	}

	@Override
	public int updateByPrimaryKeySelective(@RequestBody SysUserDto sysUserDto) {
		return sysUserDtoService.updateByPrimaryKeySelective(sysUserDto);
	}

	@Override
	public SysUserDto findByPhone(String phone) {
		return sysUserDtoService.findByPhone(phone);
	}
}
