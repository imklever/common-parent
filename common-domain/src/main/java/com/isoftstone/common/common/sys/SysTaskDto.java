package com.isoftstone.common.common.sys;

import java.util.Date;

public class SysTaskDto {
    private String id;
    private String task_name;
    private String task_code;
    private String task_type;//默认参数
    private String task_status;//默认参数
    private Date task_create_time;//默认参数
    private Date task_start_time;//默认参数
    private String task_def_param;//默认参数
    private String task_desc;//默认参数
    private String task_cron;//默认参数
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTask_name() {
        return task_name;
    }
    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }
    public String getTask_code() {
        return task_code;
    }
    public void setTask_code(String task_code) {
        this.task_code = task_code;
    }
    public String getTask_type() {
        return task_type;
    }
    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }
    public String getTask_status() {
        return task_status;
    }
    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }
    public Date getTask_create_time() {
        return task_create_time;
    }
    public void setTask_create_time(Date task_create_time) {
        this.task_create_time = task_create_time;
    }
    public Date getTask_start_time() {
        return task_start_time;
    }
    public void setTask_start_time(Date task_start_time) {
        this.task_start_time = task_start_time;
    }
    public String getTask_def_param() {
        return task_def_param;
    }
    public void setTask_def_param(String task_def_param) {
        this.task_def_param = task_def_param;
    }
    public String getTask_desc() {
        return task_desc;
    }
    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }
    public String getTask_cron() {
        return task_cron;
    }
    public void setTask_cron(String task_cron) {
        this.task_cron = task_cron;
    }
    
    
}
