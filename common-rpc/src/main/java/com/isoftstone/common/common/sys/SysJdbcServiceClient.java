package com.isoftstone.common.common.sys;

import org.common.constant.ServiceBackConstants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = ServiceBackConstants.BACK_SERVICE,
path = ServiceBackConstants.PATH_SYS_JDBC)
public interface SysJdbcServiceClient extends SysJdbcServiceDefinition {

}
