package com.isoftstone.common.networm.controller;

import com.isoftstone.common.networm.task.CompanyFinanceTask;
import com.isoftstone.common.networm.task.CompanyInfoTask;
import com.isoftstone.common.networm.task.IpProxyTask;
import com.isoftstone.common.networm.task.NewsTask;
import com.isoftstone.common.networm.util.UsedMethod;
import com.isoftstone.common.util.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/networm")
public class AutoTaskController{
    @Autowired
    JsonService jsonService;
    @Autowired
    UsedMethod usedMethod;
    @Autowired
    NewsTask newsTask;
    @Autowired
    IpProxyTask ipProxyTask;
    @Autowired
    CompanyInfoTask companyInfoTask;
    @Autowired
    CompanyFinanceTask companyFinanceTask;

    @ResponseBody
    @RequestMapping(value = "/ipProxy", method = {RequestMethod.POST,
            RequestMethod.GET})
    public Object isProxyTask(
            HttpServletRequest request,
            @RequestParam(value = "businessCode", required = true) String businesscode,
            @RequestParam(value = "params", required = false, defaultValue = "{}") String params) {
        ipProxyTask.setCode(businesscode);
        Thread thread = new Thread(ipProxyTask);
        thread.start();
        return "ip代理池抓取任务启动啦。详情请看爬虫日志！";
    }

    @ResponseBody
    @RequestMapping(value = "/newsTask", method = {RequestMethod.POST,
            RequestMethod.GET})
    public Object newsListTask(
            HttpServletRequest request,
            @RequestParam(value = "businessCode", required = true) String businesscode,
            @RequestParam(value = "params",  required = false, defaultValue = "{}") String params)  {
        Map<String,Object> paramMap = new HashMap<>();
        synchronized (this) {
            paramMap = (Map<String, Object>) jsonService.parseObject(params);
            newsTask.setCode(businesscode);
            newsTask.setParam(paramMap);
            Thread thread = new Thread(newsTask);
            thread.start();
        }
        return paramMap + "新闻资讯抓取任务启动成功,详情请看爬虫日志！";
    }

    @ResponseBody
    @RequestMapping(value = "/companyInfoTask", method = {RequestMethod.POST,
            RequestMethod.GET})
    public Object CompanyInfoTask(
            HttpServletRequest request,
            @RequestParam(value = "businessCode", required = true) String businesscode,
            @RequestParam(value = "params",  required = false, defaultValue = "{}") String params) {
        Map<String,Object> paramMap = (Map<String, Object>) jsonService.parseObject(params);
        companyInfoTask.setCode(businesscode);
        Thread thread = new Thread(companyInfoTask);
        thread.start();
        return "公司基础信息抓取任务启动啦。详情请看爬虫日志！";
    }

    @ResponseBody
    @RequestMapping(value = "/companyFinanceTask", method = {RequestMethod.POST,
            RequestMethod.GET})
    public Object CompanyFinanceTask(
            HttpServletRequest request,
            @RequestParam(value = "businessCode", required = true) String businesscode,
            @RequestParam(value = "params",  required = false, defaultValue = "{}") String params) {
        Map<String,Object> paramMap = (Map<String, Object>) jsonService.parseObject(params);
        companyFinanceTask.setCode(businesscode);
        Thread thread = new Thread(companyFinanceTask);
        thread.start();
        return "公司财务信息抓取任务启动啦。详情请看爬虫日志！";
    }
}