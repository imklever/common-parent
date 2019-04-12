package com.isoftstone.common.api.controller.wixin.bean;

import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("xml")
public class WxAppPayNotify extends BaseWxPayResult{
    private static final long serialVersionUID = 5365462210874106959L;
    @XStreamAlias("appid")
    private String appid;
    @XStreamAlias("bank_type")
    private String bankType;
    @XStreamAlias("cash_fee")
    private String cashFee;
    @XStreamAlias("fee_type")
    private String feeType;
    @XStreamAlias("is_subscribe")
    private String isSubscribe;
    @XStreamAlias("mch_id")
    private String mchId;
    @XStreamAlias("nonce_str")
    private String nonceStr;
    @XStreamAlias("openid")
    private String openid;
    @XStreamAlias("out_trade_no")
    private String outTradeNo;
    @XStreamAlias("result_code")
    private String resultCode;
    @XStreamAlias("return_code")
    private String returnCode;
    @XStreamAlias("sign")
    private String sign;
    @XStreamAlias("time_end")
    private String timeEnd;
    @XStreamAlias("total_fee")
    private String totalFee;
    @XStreamAlias("trade_type")
    private String tradeType;
    @XStreamAlias("transaction_id")
    private String transactionId;
    public String getAppid() {
        return appid;
    }
    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getCashFee() {
        return cashFee;
    }
    public void setCashFee(String cashFee) {
        this.cashFee = cashFee;
    }
    public String getFeeType() {
        return feeType;
    }
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    public String getIsSubscribe() {
        return isSubscribe;
    }
    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }
    public String getMchId() {
        return mchId;
    }
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
    public String getNonceStr() {
        return nonceStr;
    }
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public String getOutTradeNo() {
        return outTradeNo;
    }
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
    public String getResultCode() {
        return resultCode;
    }
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    public String getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getTimeEnd() {
        return timeEnd;
    }
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
    public String getTotalFee() {
        return totalFee;
    }
    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }
    public String getTradeType() {
        return tradeType;
    }
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
}
