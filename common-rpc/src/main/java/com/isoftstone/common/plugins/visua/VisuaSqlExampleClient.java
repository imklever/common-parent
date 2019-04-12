package com.isoftstone.common.plugins.visua;



import org.common.constant.ServiceBackConstants;
import org.springframework.cloud.openfeign.FeignClient;



@FeignClient(value = ServiceBackConstants.BACK_SERVICE,
            path = ServiceBackConstants.PATH_VISUA_SQL)
public interface VisuaSqlExampleClient extends VisuaSqlExampleDefinition{


}
