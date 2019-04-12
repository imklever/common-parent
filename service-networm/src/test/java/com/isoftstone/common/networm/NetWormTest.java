package com.isoftstone.common.networm;

import com.isoftstone.common.networm.db.CommonDB;
import com.isoftstone.common.networm.job.CommonJob;
import com.isoftstone.common.networm.job.CompanyInfoJob;
import com.isoftstone.common.networm.job.RelatedInfoJob;
import com.isoftstone.common.networm.util.UsedMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NetWormTest {

    /*@Autowired
    JsonService jsonService;
    @Autowired
    CommBusinessRunService commBusinessRunService;
    @Autowired
    VisuaSqlExampleMapper visuaSqlExampleMapper;*/
    @Autowired
    UsedMethod usedMethod;
    @Autowired
    CompanyInfoJob  companyInfoJob;
    @Autowired
    RelatedInfoJob relatedInfoJob;
    @Autowired
    CommonJob commonJob;
    @Autowired
    CommonDB commonDB;

    @Test
    public void test(){
        /*companyInfoJob.setCode("S12039");
        companyInfoJobfoJob.CompanyInfoList();*/
        companyInfoJob.companyFinanceInfoJob();
    }
}
