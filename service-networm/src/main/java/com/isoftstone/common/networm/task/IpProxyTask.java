package com.isoftstone.common.networm.task;

import com.isoftstone.common.networm.util.UsedMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IpProxyTask extends Thread  {

    @Autowired
    UsedMethod usedMethod;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String code = "";

    public void setCode(String code) {
        this.code = code;
    }

    public void run(){
        logger.info("ip代理池定时任务开始");
        long start = System.currentTimeMillis();
        usedMethod.setCode(code);
        usedMethod.saveProxy();
        long end = System.currentTimeMillis();
        logger.info("ip代理池定时任务结束，耗时:" + (end -start)/1000 + "秒。");
    }
}
