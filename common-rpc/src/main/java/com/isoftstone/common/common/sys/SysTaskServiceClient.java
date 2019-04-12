package com.isoftstone.common.common.sys;

import org.common.constant.ServiceBackConstants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = ServiceBackConstants.TASK_SERVICE,
path = ServiceBackConstants.PATH_SYS_TASK)
public interface SysTaskServiceClient extends SysTaskServiceDefinition{

}
