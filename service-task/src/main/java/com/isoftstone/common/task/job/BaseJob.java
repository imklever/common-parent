package com.isoftstone.common.task.job;

import java.util.Date;

import org.common.constant.CommonConstants;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.isoftstone.common.common.sys.SysTaskDetailDto;
import com.isoftstone.common.common.sys.SysTaskDto;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.service.CommBusinessRunService;
import com.isoftstone.common.task.mapper.VisuaSqlExampleMapper;
import com.isoftstone.common.util.IdService;
import com.isoftstone.common.util.JsonService;
/**
 * 基础任务类
 * @author cpniuc
 *
 */
public abstract class BaseJob implements Job{
    @Autowired
    JsonService jsonService;
    @Autowired
    VisuaSqlExampleMapper visuaSqlExampleMapper;
    @Autowired
    CommBusinessRunService commBusinessRunService;
    @Autowired
    private IdService idService;
    public abstract String subExecute(JobExecutionContext context)  throws Exception;
    
    public  void execute(JobExecutionContext context) throws JobExecutionException{
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String jobdata = jobDataMap.getString(CommonConstants.TASK_DATA_PARM_JOB_DATA);
        SysTaskDto sysTaskDto= jsonService.parseObject(jobdata, SysTaskDto.class);
        VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode("I06004");
        SysTaskDetailDto sysTaskDetailDto=new SysTaskDetailDto(idService.newOne(),
                    sysTaskDto.getId(),CommonConstants.TASK_RUN_STATUS_RUNNING);
        try {
            commBusinessRunService.getByParamBusinessCode(visuaSqlExample, 
                    jsonService.toJson(sysTaskDetailDto), null, null);
            sysTaskDetailDto.setJob_log(subExecute(context));
            sysTaskDetailDto.setTask_over_status(CommonConstants.TASK_RESULT_STATUS_SUCCESS);
        } catch (Exception e) {
            sysTaskDetailDto.setTask_over_status(CommonConstants.TASK_RESULT_STATUS_FAIL);
            sysTaskDetailDto.setJob_log(e.getMessage());
            System.out.println(e.getMessage()+"=++++++++++++");
            e.printStackTrace();
        }
        sysTaskDetailDto.setTask_status(CommonConstants.TASK_RUN_STATUS_END);
        visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode("U06004");
        try {
            commBusinessRunService.getByParamBusinessCode(visuaSqlExample, 
                    jsonService.toJson(sysTaskDetailDto), null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
