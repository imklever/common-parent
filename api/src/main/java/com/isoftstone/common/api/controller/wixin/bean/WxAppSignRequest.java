package com.isoftstone.common.api.controller.wixin.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class WxAppSignRequest {
	@XStreamAlias("appid")
    private String appid;
	@XStreamAlias("partnerid")
    private String partnerid;
	@XStreamAlias("prepayid")
    private String prepayid;
	@XStreamAlias("package")
    private String packAge;
	@XStreamAlias("noncestr")
    private String noncestr;
	@XStreamAlias("timestamp")
    private String timestamp;
	@XStreamAlias("sign")
    private String sign;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getPackAge() {
		return packAge;
	}
	public void setPackAge(String packAge) {
		this.packAge = packAge;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
