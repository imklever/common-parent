package com.isoftstone.common.common.sys;

public class RunBusinessCodeJobDto {
    private String businessCode;
    private String defParms;
    private String runParms;
    public String getBusinessCode() {
        return businessCode;
    }
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
    public String getDefParms() {
        return defParms==null?"{}":defParms;
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
    
    
}
