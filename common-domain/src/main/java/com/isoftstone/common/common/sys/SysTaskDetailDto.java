package com.isoftstone.common.common.sys;

public class SysTaskDetailDto {
    private String id;
    private String task_id;
    private String task_start_time;
    private String task_end_time;
    private String task_status;
    private String task_over_status;
    private String job_log;
    
    
    public SysTaskDetailDto(String id, String task_id, String task_status) {
        super();
        this.id = id;
        this.task_id = task_id;
        this.task_status = task_status;
    }
    public SysTaskDetailDto() {
        super();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTask_id() {
        return task_id;
    }
    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }
    public String getTask_start_time() {
        return task_start_time;
    }
    public void setTask_start_time(String task_start_time) {
        this.task_start_time = task_start_time;
    }
    public String getTask_end_time() {
        return task_end_time;
    }
    public void setTask_end_time(String task_end_time) {
        this.task_end_time = task_end_time;
    }
    public String getTask_status() {
        return task_status;
    }
    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }
    public String getTask_over_status() {
        return task_over_status;
    }
    public void setTask_over_status(String task_over_status) {
        this.task_over_status = task_over_status;
    }
    public String getJob_log() {
        return job_log;
    }
    public void setJob_log(String job_log) {
        this.job_log = job_log;
    }
    
    
}
