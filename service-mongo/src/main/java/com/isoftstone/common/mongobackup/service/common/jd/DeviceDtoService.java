package com.isoftstone.common.mongobackup.service.common.jd;

import java.util.List;

import com.isoftstone.common.mongobackup.domain.mysql.DeviceDto;
import com.isoftstone.common.mongobackup.service.BaseService;
 
public interface DeviceDtoService extends BaseService<DeviceDto, String>{
	List<DeviceDto> selectAll();
	List<DeviceDto> selectByTypeId(String typeId);
}
