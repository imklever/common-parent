package com.isoftstone.common.task.controller;

import java.util.Map;

import org.common.constant.ServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.common.sys.SysTaskServiceDefinition;
import com.isoftstone.common.task.service.SysTaskService;
@RefreshScope
@RestController
@RequestMapping(name = ServiceBackConstants.TASK_SERVICE,
    path = ServiceBackConstants.PATH_SYS_TASK)
public class SysTaskController  implements SysTaskServiceDefinition{
    @Autowired
    SysTaskService sysTaskService;

    @Override
    public Map<String, Object> delJob(@RequestBody String dtoJson) {
        return sysTaskService.delJob(dtoJson);
    }

    @Override
    public Map<String, Object> startJob(@RequestBody String dtoJson) {
        return sysTaskService.startJob(dtoJson);
    }

    @Override
    public Map<String, Object> stopJob(@RequestBody String dtoJson) {
        return sysTaskService.stopJob(dtoJson);
    }

    @Override
    public Map<String, Object> restartJob(@RequestBody String dtoJson) {
        return sysTaskService.reStartJob(dtoJson);
    }

    @Override
    public Map<String, Object> rescheduleJob( @RequestBody String dtoJson) {
        return sysTaskService.rescheduleJob(dtoJson);
    }

}
