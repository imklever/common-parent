package com.isoftstone.common.task.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.common.constant.CommonConstants;
import org.common.constant.ErrorCodeConstants;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.isoftstone.common.common.sys.SysTaskDto;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.service.CommBusinessRunService;
import com.isoftstone.common.service.CommonSqlService;
import com.isoftstone.common.task.job.BaseJob;
import com.isoftstone.common.task.mapper.VisuaSqlExampleMapper;
import com.isoftstone.common.task.service.SysTaskService;
import com.isoftstone.common.util.JsonService;

@Service
public class SysTaskServiceImpl implements SysTaskService {
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;
    @Autowired
    private JsonService jsonService;
    @Autowired
    CommonSqlService commonSqlService;
    @Autowired
    CommBusinessRunService commBusinessRunService;
    @Autowired
    VisuaSqlExampleMapper visuaSqlExampleMapper;
    
    private void cheangeTaskStatus(SysTaskDto sysTaskDto) throws Exception {
        VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode("U06003");
        commBusinessRunService.getByParamBusinessCode(visuaSqlExample, 
                jsonService.toJson(sysTaskDto), null, null);
    }

    private SysTaskDto getTaskMsg(String dtoJson) throws Exception {
        VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode("S06003");
        Map<String, Object> map = null;
            map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample, dtoJson, null, null);
        SysTaskDto sysTaskDto=null;
        if (map != null && map.containsKey(CommonConstants.BUSINESS_DEF_KEY_DATA_LIST)) {
           List<Map<String, Object>> rows= (List<Map<String, Object>>)
                       map.get(CommonConstants.BUSINESS_DEF_KEY_DATA_LIST);
           if(rows!=null&&rows.size()==1) {
               Map<String, Object> obj=rows.get(0);
               sysTaskDto = jsonService.parseObject(jsonService.toJson(obj), SysTaskDto.class);
           }else {
               throw new RuntimeException("任务 ID不存在!");
           }
        }else {
            throw new RuntimeException("任务 ID不存在!或系统配置错误，请联系管理员！");
        }
        return sysTaskDto;
    }

    @Override
    public Map<String, Object> delJob(String dtoJson) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            SysTaskDto sysTaskDto = getTaskMsg(dtoJson);
            if(sysTaskDto.getTask_status().
                    equals(CommonConstants.TASK_RUN_STATUS_READY)) {
                map.put(ErrorCodeConstants.HASH_ERR,"任务现在为"+sysTaskDto.getTask_status()+",未开始的任务不用删除调度！");
                return map;
            } 
            scheduler.pauseTrigger(TriggerKey.triggerKey(sysTaskDto.getTask_code(), sysTaskDto.getTask_type()));
            scheduler.unscheduleJob(TriggerKey.triggerKey(sysTaskDto.getTask_code(), sysTaskDto.getTask_type()));
            scheduler.deleteJob(JobKey.jobKey(sysTaskDto.getTask_code(), sysTaskDto.getTask_type()));
            sysTaskDto.setTask_status(CommonConstants.TASK_RUN_STATUS_READY);
            cheangeTaskStatus(sysTaskDto);
        } catch (Exception e) {
            map.put(ErrorCodeConstants.HASH_ERR, e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, Object> startJob(String dtoJson) {
        // 启动调度器
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (!scheduler.isShutdown()) {  
                scheduler.start();  
            }  
            SysTaskDto sysTaskDto = getTaskMsg(dtoJson);
            if(!sysTaskDto.getTask_status().
                        equals(CommonConstants.TASK_RUN_STATUS_READY)) {
                map.put(ErrorCodeConstants.HASH_ERR,"任务现在为"+sysTaskDto.getTask_status()+",只有未开始的才能运行！");
                return map;
            } 
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(sysTaskDto.getTask_type()).getClass())
                    .withIdentity(sysTaskDto.getTask_code(), sysTaskDto.getTask_type())
                    .usingJobData(CommonConstants.TASK_DATA_PARM_DEF_DATA, sysTaskDto.getTask_def_param())
                    .usingJobData(CommonConstants.TASK_DATA_PARM_JOB_DATA, jsonService.toJson(sysTaskDto)).build();

            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysTaskDto.getTask_cron());
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(sysTaskDto.getTask_code(), sysTaskDto.getTask_type()).withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            sysTaskDto.setTask_start_time(new Date());
            sysTaskDto.setTask_status(CommonConstants.TASK_RUN_STATUS_RUNNING);
            cheangeTaskStatus(sysTaskDto);
            // SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow().build();
            /*
             * 简单调度器 // 在当前时间15秒后运行 Date startTime = DateBuilder.nextGivenSecondDate(new
             * Date( ),15); // 创建一个SimpleTrigger实例，指定该Trigger在Scheduler中所属组及名称。 //
             * 接着设置调度的时间规则.当前时间15秒后运行，每10秒运行一次，共运行5次 SimpleTrigger trigger = (SimpleTrigger)
             * TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
             * .startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule()
             * .withIntervalInSeconds(10) // 时间间隔 .withRepeatCount(5) //重复时间 ).build();
             */

        } catch (Exception e) {
            map.put(ErrorCodeConstants.HASH_ERR, e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, Object> stopJob(String dtoJson) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            SysTaskDto sysTaskDto = getTaskMsg(dtoJson);
            if(!sysTaskDto.getTask_status().
                    equals(CommonConstants.TASK_RUN_STATUS_RUNNING)) {
                map.put(ErrorCodeConstants.HASH_ERR,"任务现在为"+sysTaskDto.getTask_status()+",只有运行中的任务才能暂停！");
                return map;
            } 
            scheduler.pauseJob(JobKey.jobKey(sysTaskDto.getTask_code(), sysTaskDto.getTask_type()));
            sysTaskDto.setTask_status(CommonConstants.TASK_RUN_STATUS_STOP);
            cheangeTaskStatus(sysTaskDto);
        } catch (Exception e) {
            map.put(ErrorCodeConstants.HASH_ERR, e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, Object> reStartJob(String dtoJson) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            SysTaskDto sysTaskDto = getTaskMsg(dtoJson);
            if(!sysTaskDto.getTask_status().
                    equals(CommonConstants.TASK_RUN_STATUS_STOP)) {
                map.put(ErrorCodeConstants.HASH_ERR,"任务现在为"+sysTaskDto.getTask_status()+",只有暂停中的任务才能重启！");
            return map;
            }
            scheduler.resumeJob(JobKey.jobKey(sysTaskDto.getTask_code(), sysTaskDto.getTask_type()));
            sysTaskDto.setTask_status(CommonConstants.TASK_RUN_STATUS_RUNNING);
            cheangeTaskStatus(sysTaskDto);
        } catch (Exception e) {
            map.put(ErrorCodeConstants.HASH_ERR, e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, Object> rescheduleJob(String dtoJson) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            SysTaskDto sysTaskDto = getTaskMsg(dtoJson);
            if(!sysTaskDto.getTask_status().
                    equals(CommonConstants.TASK_RUN_STATUS_RUNNING)) {
                map.put(ErrorCodeConstants.HASH_ERR,"任务现在为"+sysTaskDto.getTask_status()+",未启动的任务不能执行刷新调度操作！");
                return map;
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(sysTaskDto.getTask_code(), sysTaskDto.getTask_type());
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysTaskDto.getTask_cron());

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            sysTaskDto.setTask_status(CommonConstants.TASK_RUN_STATUS_RUNNING);
            cheangeTaskStatus(sysTaskDto);
        } catch (Exception e) {
            map.put(ErrorCodeConstants.HASH_ERR, e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    public static BaseJob getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob) class1.newInstance();
    }

}
