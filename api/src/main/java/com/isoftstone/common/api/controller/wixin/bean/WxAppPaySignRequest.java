package com.isoftstone.common.api.controller.wixin.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
public class WxAppPaySignRequest{
    @XStreamAlias("appId")
    private String appid;
    @XStreamAlias("timeStamp")
    private String timeStamp;
    @XStreamAlias("nonceStr")
    private String nonceStr;
    @XStreamAlias("package")
    private String packAge;
    @XStreamAlias("signType")
    private String signType;
    @XStreamAlias("paySign")
    private String paySign;
    public String getAppid() {
        return appid;
    }
    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getNonceStr() {
        return nonceStr;
    }
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }
    public String getPackAge() {
        return packAge;
    }
    public void setPackAge(String packAge) {
        this.packAge = packAge;
    }
    public String getSignType() {
        return signType;
    }
    public void setSignType(String signType) {
        this.signType = signType;
    }
    public String getPaySign() {
        return paySign;
    }
    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
    
    
}
