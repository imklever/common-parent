package com.isoftstone.common.task.job.impl;

import java.util.Map;

import org.common.constant.CommonConstants;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.isoftstone.common.common.sys.RunBySerivceAndUrlJobDto;
import com.isoftstone.common.task.job.BaseJob;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.SpringUtil;

public class RunBySerivceAndUrlJob  extends BaseJob{
    @Autowired
    JsonService jsonService;
    @Autowired
    RestTemplate restTemplate;
    @Override
    public String subExecute(JobExecutionContext context) throws Exception {
        LoadBalancerClient loadBalancerClient=SpringUtil.getBean(LoadBalancerClient.class);
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String jobdata = jobDataMap.getString(CommonConstants.TASK_DATA_PARM_DEF_DATA);
        RunBySerivceAndUrlJobDto runBySerivceAndUrlJobDto=jsonService.
                parseObject(jobdata, RunBySerivceAndUrlJobDto.class);
        String serviceId=runBySerivceAndUrlJobDto.getServiceName();
        String path=runBySerivceAndUrlJobDto.getPath();
        ServiceInstance serviceInstance =loadBalancerClient.choose(serviceId);
        String url="http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()+path;
        Map<String,Object>parmdss=jsonService.parseMap(runBySerivceAndUrlJobDto.getDefParms());
        System.out.println(jsonService.toJson(runBySerivceAndUrlJobDto));
        if("post".equals(runBySerivceAndUrlJobDto.getHttpMethod())) {
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
            for (String key : parmdss.keySet()) {
                params.add(key, jsonService.toJson(parmdss.get(key)));
            }
            HttpEntity<MultiValueMap<String, String>> requestEntity =
                    new HttpEntity<MultiValueMap<String, String>>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, requestEntity,String.class);
            return response.getBody();
        }else {
            throw new RuntimeException( "暂未支持--待修改代码");
        }   
        
    }
}
