package com.isoftstone.common.common.sys.hystrix;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.common.sys.SysTaskServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class HystrixSysTaskServiceClient implements SysTaskServiceClient{
    @Autowired
    SysTaskServiceClient sysTaskServiceClient;
    @Override
    @HystrixCommand(fallbackMethod = "delJobFallBackCall")
    public Map<String, Object> delJob(String dtoJson) {
        return sysTaskServiceClient.delJob(dtoJson);
    }
    public Map<String, Object> delJobFallBackCall(String dtoJson) {
        return null;
    }

    @Override
    @HystrixCommand(fallbackMethod = "startJobFallBackCall")
    public Map<String, Object> startJob(String dtoJson) {
        return sysTaskServiceClient.startJob(dtoJson);
    }
    public Map<String, Object> startJobFallBackCall(String dtoJson) {
        return null;
    }

    @Override
    @HystrixCommand(fallbackMethod = "restartJobFallBackCall")
    public Map<String, Object> restartJob(String dtoJson) {
        return sysTaskServiceClient.restartJob(dtoJson);
    }
    public Map<String, Object> restartJobFallBackCall(String dtoJson) {
        return null;
    }

    @Override
    @HystrixCommand(fallbackMethod = "rescheduleJobFallBackCall")
    public Map<String, Object> rescheduleJob(String dtoJson) {
        return sysTaskServiceClient.rescheduleJob(dtoJson);
    }
    public Map<String, Object> rescheduleJobFallBackCall(String dtoJson) {
        return null;
    }

    @Override
    @HystrixCommand(fallbackMethod = "stopJobFallBackCall")
    public Map<String, Object> stopJob(String dtoJson) {
        return sysTaskServiceClient.stopJob(dtoJson);
    }
    public Map<String, Object> stopJobFallBackCall(String dtoJson) {
        return null;
    }

}
