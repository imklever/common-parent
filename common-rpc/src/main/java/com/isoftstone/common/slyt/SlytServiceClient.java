package com.isoftstone.common.slyt;

import org.common.constant.ServiceBackConstants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = ServiceBackConstants.BACK_SERVICE)
public interface SlytServiceClient extends SlytAiDefinition {

}
