package com.isoftstone.common.common.sys;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface SysTaskServiceDefinition {
     @RequestMapping(value = "/delJob", method =  { RequestMethod.POST})
     Map<String, Object> delJob(@RequestBody String dtoJson);
     @RequestMapping(value = "/startJob", method = { RequestMethod.POST})
     Map<String, Object> startJob(@RequestBody String dtoJson);
     @RequestMapping(value = "/restartJob", method = { RequestMethod.POST})
     Map<String, Object> restartJob(@RequestBody String dtoJson);
     @RequestMapping(value = "/rescheduleJob", method = { RequestMethod.POST})
     Map<String, Object> rescheduleJob(@RequestBody String dtoJson);
     @RequestMapping(value = "/stopJob", method =  {RequestMethod.POST})
     Map<String, Object> stopJob(@RequestBody String dtoJson);
}
