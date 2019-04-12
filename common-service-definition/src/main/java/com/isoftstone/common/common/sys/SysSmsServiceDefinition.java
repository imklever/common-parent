package com.isoftstone.common.common.sys;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


public interface SysSmsServiceDefinition {
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    void sendMessage(@RequestParam(value = "name", required = true) Map<String, Object> datas
    );

    @RequestMapping(value = "/testSendMessage", method = RequestMethod.POST)
    void testSendMessage(@RequestParam(value = "name", required = true) Map<String, Object> datas
    );
}
