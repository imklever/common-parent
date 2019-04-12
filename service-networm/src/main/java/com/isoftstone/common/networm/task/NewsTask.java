package com.isoftstone.common.networm.task;

import com.isoftstone.common.networm.db.CommonDB;
import com.isoftstone.common.networm.job.CommonJob;
import com.isoftstone.common.networm.util.UsedMethod;
import com.isoftstone.common.util.JsonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsTask extends Thread {
    @Autowired
    CommonDB commonDB;
    @Autowired
    CommonJob commonJob;
    @Autowired
    JsonService jsonService;
    @Autowired
    UsedMethod usedMethod;
    private Logger logger = LoggerFactory.getLogger(getClass());
    String code = "";
    Map<String,Object> param = new HashMap<>();

    public void setCode(String code) {
        this.code = code;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public void run() {
        String gjCode = code;
        Map<String,Object> gjParam = new HashMap<>();
        gjParam.putAll(param);
        logger.info(gjParam + "新闻资讯任务开始!");
        long start = System.currentTimeMillis();

        //取出定时任务需要用到的businessCodes
        Map<String,Object> map = new HashMap<>();
        map.put("TaskDesc","List+Detial");
        List<Map<String,Object>> businessList = commonDB.selectMethod(gjCode,map);
        if(businessList.size() == 1){
            String[] codes = businessList.get(0).get("TaskBusinessCodes").toString().split(",");
            commonJob.setCodes(codes[0]);
            gjParam.put("type",0);
            String targetTable = usedMethod.getTargetTable(gjParam,codes[codes.length-1],"targetTable");

            //将规则生成的请求url放入target表中
            logger.info(gjParam + "生成请求url任务开始");
            List<Map<String,Object>> ruleList = commonDB.getNetWormRules(gjParam,codes[1]);
            for(Map<String,Object> m :ruleList){
                commonJob.saveListRequestUrlJob(m,targetTable);
            }
            logger.info(gjParam + "生成请求url任务结束。");

            //step1：按请求url表请求，将文章列表响应存入 caict_networm_resopnse_raw表
            logger.info(gjParam + "新闻列表抓取开始");
            gjParam.put("targetTable",targetTable);
            int num0 = commonDB.getResponseRawNum(codes[2],gjParam);
            for(int i=0;i<Math.ceil(num0/100.0);i++){
                gjParam.put("start",i * 100);
                gjParam.put("count",100);
                System.out.println("从"+i * 100+"到"+(i+1)*100);
                for(Map<String,Object> m :commonDB.selectMethod(codes[3],gjParam))
                    commonJob.responseListJob(m);
            }
            logger.info(gjParam + "新闻列表抓取结束。");

            //step2：从caict_networm_resopnse_raw表解析文章列表数据，存入targetTable表
            logger.info(gjParam + "新闻列表解析开始");
            int num = commonDB.getResponseRawNum(codes[4],gjParam);
            for(int i=0;i<Math.ceil(num/10.0);i++){
                gjParam.put("start",i * 10);
                gjParam.put("count",10);
                System.out.println("从"+i * 10+"到"+(i+1)*10);
                commonJob.parseInfoJob(commonDB.selectMethod(codes[5],gjParam),targetTable);
            }
            logger.info(gjParam + "新闻列表解析入库为：" + targetTable);
            logger.info(gjParam + "新闻列表解析结束");

            //step3：从targetTable表中查询文章详情url，再请求，最后将文章详情响应结果存入caict_networm_resopnse_raw表
            gjParam.put("type",1);
            logger.info(gjParam + "新闻详情抓取开始");
            gjParam.put("targetTable",targetTable);
            int num1 = commonDB.getResponseRawNum(codes[6],gjParam);
            for(int i=0;i<Math.ceil(num1/100.0);i++){
                gjParam.put("start",i * 100);
                gjParam.put("count",100);
                System.out.println("从"+i * 100+"到"+(i+1)*100);
                commonJob.responseDetialInfoJob(commonDB.selectMethod(codes[7],gjParam));
            }
            logger.info(gjParam + "新闻资讯抓取结束");

            //step4：从caict_networm_resopnse_raw表解析文章详情数据，存入targetTable表
            logger.info(gjParam + "新闻资讯解析开始");
            targetTable = usedMethod.getTargetTable(gjParam,codes[codes.length-1],"targetTable");
            int num2 = commonDB.getResponseRawNum(codes[8],gjParam);
            for(int i=0;i<Math.ceil(num2/10.0);i++){
                gjParam.put("start",i * 10);
                gjParam.put("count",10);
                System.out.println("从"+i * 10+"到"+(i+1)*10);
                commonJob.parseInfoJob(commonDB.selectMethod(codes[9],gjParam),targetTable);
            }
            logger.info(gjParam + "新闻资讯解析入库为：" + targetTable);
            logger.info(gjParam + "新闻资讯解析结束");
        }else {
            logger.info("任务调度的businessCodes不存在或不唯一！请更改");
        }
        long end = System.currentTimeMillis();
        logger.info(gjParam + "新闻资讯任务结束。耗时："+(end-start)/1000+"秒");
    }
}
