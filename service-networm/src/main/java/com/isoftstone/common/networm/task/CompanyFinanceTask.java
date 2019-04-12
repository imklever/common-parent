package com.isoftstone.common.networm.task;

import com.isoftstone.common.networm.job.CompanyInfoJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyFinanceTask extends Thread {

    @Autowired
    CompanyInfoJob companyInfoJob;

    private Logger logger = LoggerFactory.getLogger(getClass());

    String code = "";

    public void setCode(String code) {
        this.code = code;
    }

    public void run() {
        logger.info("公司财务信息定时任务开始");
        companyInfoJob.setCode(code);
        companyInfoJob.companyFinanceInfoJob();
        logger.info("公司财务信息定时任务结束");
    }
}
