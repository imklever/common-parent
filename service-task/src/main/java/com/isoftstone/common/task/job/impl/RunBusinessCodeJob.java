package com.isoftstone.common.task.job.impl;

import java.util.HashMap;
import java.util.Map;

import org.common.constant.CommonConstants;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.isoftstone.common.common.sys.RunBusinessCodeJobDto;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.service.CommBusinessRunService;
import com.isoftstone.common.task.job.BaseJob;
import com.isoftstone.common.task.mapper.VisuaSqlExampleMapper;
import com.isoftstone.common.util.JsonService;

public class RunBusinessCodeJob extends BaseJob {
    @Autowired
    CommBusinessRunService commBusinessRunService;

    @Autowired
    JsonService jsonService;
    @Autowired
    VisuaSqlExampleMapper visuaSqlExampleMapper;

    public String subExecute(JobExecutionContext context) throws Exception {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String defParms = jobDataMap.getString(CommonConstants.TASK_DATA_PARM_DEF_DATA);
        System.out.println(defParms);
        RunBusinessCodeJobDto runBusinessCodeJobDto= jsonService.parseObject(defParms, RunBusinessCodeJobDto.class);
        VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(runBusinessCodeJobDto.getBusinessCode());
        Map<String, Object> map = new HashMap<String, Object>();
        map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample, runBusinessCodeJobDto.getDefParms(), null, null);
        return jsonService.toJson(map);
    }



}
