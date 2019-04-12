package com.isoftstone.common.api.service.plugins.visua;

import com.isoftstone.common.common.sys.SysUserDto;

public interface SysUserService {

	SysUserDto findByUserPass(String name,String password);

}
