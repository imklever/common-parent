package com.isoftstone.common.common;

import org.common.constant.ServiceBackConstants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = ServiceBackConstants.BACK_SERVICE,
path = ServiceBackConstants.PATH_COMMON_HTTP_LOG)
public interface SysHttpLogServiceClient extends SysHttpLogServiceDefinition {

}
