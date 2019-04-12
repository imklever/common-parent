package com.isoftstone.common.common.sys;

public class RunBySerivceAndUrlJobDto {
    private String serviceName;
    private String path;
    private String defParms;
    private String runParms;
    private String httpMethod;
    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getDefParms() {
        return defParms;
    }
    public void setDefParms(String defParms) {
        this.defParms = defParms;
    }
    public String getRunParms() {
        return runParms;
    }
    public void setRunParms(String runParms) {
        this.runParms = runParms;
    }
    public String getHttpMethod() {
        return httpMethod;
    }
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
    
    
    
    
}
