package com.isoftstone.common.task.service;

import java.util.Map;

public interface SysTaskService {


    Map<String, Object> delJob(String dtoJson);

    Map<String, Object> startJob(String dtoJson);

    Map<String, Object> stopJob(String dtoJson);
    
    Map<String, Object> reStartJob(String dtoJson);
    
    Map<String, Object> rescheduleJob(String dtoJson);

}
