package com.isoftstone.common.mongo;

import org.common.constant.MongoServiceBackConstants;
import org.common.constant.ServiceBackConstants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = MongoServiceBackConstants.BACK_SERVICE,
path = MongoServiceBackConstants.PATH_SYS_FILE)
public interface FileServiceClient extends FileServiceDefinition {

}
