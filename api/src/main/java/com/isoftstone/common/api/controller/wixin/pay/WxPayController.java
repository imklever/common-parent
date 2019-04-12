package com.isoftstone.common.api.controller.wixin.pay;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import com.isoftstone.common.api.controller.wixin.bean.WxAppPaySignRequest;
import com.isoftstone.common.api.controller.wixin.bean.WxAppSignRequest;
import com.isoftstone.common.api.controller.wixin.bean.WxConfigBean;
import com.isoftstone.common.api.controller.wixin.service.WxAppService;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.WxPayUtils;
import org.common.constant.ApiMapperUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiMapperUrlConstants.WX_APP_PAY)
public class WxPayController {
	@Autowired
	WxPayService wxPayService;
	@Autowired
	JsonService jsonService;
    @Autowired
    WxAppService wxAppService;
	/*String appid="wxf8c5ee0b29f0fb65";//小程序
	String mchid="1502830891";//商户号
	String appSecret="2918b847e119f5cb962556efa0a731d2";
	String signKey="193806250b4c09247ec02edce69f6a2d";*/
    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
    
    
   /* String appid="wx58a78ead17454375";//小程序
	String mchid="1323235501";//商户号
	String appSecret="ac069966def160aac553dd00964f9f27";
	String signKey="01eac548fe4b15fb5a3f1ddd265c0122";
	String NotifyUrl="https://wxapp.diyifanghu.com/comm-top/wx/app/pay/notify";
	String ip="47.92.203.69";*/
	
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
	
	@RequestMapping(value = "/preAppPay", method = { RequestMethod.POST, RequestMethod.GET })
    public Object appPrePay(@RequestParam(value = "openid", required = true)String  openid,
            @RequestParam(value = "businessCode", required = true) String businesscode,
            @RequestParam(value = "appId", required = true) String appId,
			@RequestParam(value = "params", required = false, defaultValue = "{}")String params) throws Exception {
		WxConfigBean wxConfigBean=   wxAppService.getWxConfigBean(appId);
		System.out.println(jsonService.toJson(wxConfigBean));
		 
		WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest=new WxPayUnifiedOrderRequest();
		Map<String, Object> map=hystrixVisuaSqlExampleClient.getByDataBusinessCode(businesscode, params);
		if(map.containsKey("obj")) {
		  List<Map<String, Object>> rows=(List<Map<String, Object>>) map.get("obj");
		  if(rows!=null&&rows.size()==1) {
			  Map<String, Object> row=rows.get(0);
			  wxPayUnifiedOrderRequest.setBody(row.get("body").toString());
			  if(row.get("totalFee") instanceof Double) {
				  Double d=(Double) row.get("totalFee");
				  wxPayUnifiedOrderRequest.setTotalFee(d.intValue());  
			  }else {
				  wxPayUnifiedOrderRequest.setTotalFee((int)row.get("totalFee"));  
			  }
			  wxPayUnifiedOrderRequest.setOutTradeNo((String) row.get("outTradeNo"));//商户订单号需要修改
			  wxPayUnifiedOrderRequest.setAttach((String) row.get("attach"));
		  }else {
			  return APIResult.createInstance("下单失败，找不到对应的订单号！");
		  }
		}else {
			return APIResult.createInstance("下单失败，找不到对应的订单号！sql有误！");
		}
	    WxPayConfig config=new WxPayConfig();
	    config.setAppId(wxConfigBean.getApp_id());
	    config.setMchKey(wxConfigBean.getSign_key() );
	    config.setMchId(wxConfigBean.getMchid() );
	    wxPayService.setConfig(config);
	    wxPayUnifiedOrderRequest.setMchId(wxConfigBean.getMchid());
        wxPayUnifiedOrderRequest.setAppid(wxConfigBean.getApp_id());
	    wxPayUnifiedOrderRequest.setNonceStr(WxPayUtils.getNonceStr());//随机数
	    wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.APP);
	    wxPayUnifiedOrderRequest.setSpbillCreateIp(wxConfigBean.getIp());
	    wxPayUnifiedOrderRequest.setNotifyUrl(wxConfigBean.getNotify_url()+"/"+wxConfigBean.getApp_id());
	    wxPayUnifiedOrderRequest.setOpenid(openid);
	    WxPayUnifiedOrderResult  obj= wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
	    
	    WxAppSignRequest wxAppPaySignRequest=new WxAppSignRequest();
	    wxAppPaySignRequest.setAppid(appId);
	    wxAppPaySignRequest.setNoncestr(UUID.randomUUID().toString().substring(4));
	    wxAppPaySignRequest.setTimestamp(System.currentTimeMillis()/1000+"");
	    wxAppPaySignRequest.setPackAge("Sign=WXPay");
	    wxAppPaySignRequest.setPartnerid(wxConfigBean.getMchid());
	    wxAppPaySignRequest.setPrepayid(obj.getPrepayId());
	    wxAppPaySignRequest.setSign(SignUtils.createSign(wxAppPaySignRequest, null,wxConfigBean.getSign_key(), false));
       return APIResult.createSuccess(wxAppPaySignRequest);
       
       /*	
	   	WxPayConfig config=new WxPayConfig();
	    config.setAppId("wx498d55db157f73dd");
	    config.setMchKey("01eac548fe4b15fb5a3f1ddd265c0122");
	    config.setMchId("1323235501");
	    wxPayService.setConfig(config);
	    WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest=new WxPayUnifiedOrderRequest();
	    wxPayUnifiedOrderRequest.setMchId("1323235501");
        wxPayUnifiedOrderRequest.setAppid("wx498d55db157f73dd");
	    wxPayUnifiedOrderRequest.setNonceStr(WxPayUtils.getNonceStr());//随机数
	    wxPayUnifiedOrderRequest.setBody("标题11_body");//标题
	    wxPayUnifiedOrderRequest.setTotalFee(1); //金额
	    wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.APP);
	    wxPayUnifiedOrderRequest.setSpbillCreateIp("47.92.203.69");
	    wxPayUnifiedOrderRequest.setNotifyUrl("https://wxapp.diyifanghu.com/comm-top/wx/app/pay/notify/"+"wx498d55db157f73dd");
	    wxPayUnifiedOrderRequest.setOpenid(openid);
	    wxPayUnifiedOrderRequest.setOutTradeNo(sdf.format(new Date()));//商户订单号需要修改
	    wxPayUnifiedOrderRequest.setAttach("attach");
	    WxPayUnifiedOrderResult  obj= wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
	    Map<String , Object>mape =new HashMap<String, Object>();
	    WxAppSignRequest wxAppPaySignRequest=new WxAppSignRequest();
	    wxAppPaySignRequest.setAppid("wx498d55db157f73dd");
	    wxAppPaySignRequest.setNoncestr(UUID.randomUUID().toString().substring(4));
	    wxAppPaySignRequest.setTimestamp(System.currentTimeMillis()/1000+"");
	    wxAppPaySignRequest.setPackAge("Sign=WXPay");
	    wxAppPaySignRequest.setPartnerid("1323235501");
	    wxAppPaySignRequest.setPrepayid(obj.getPrepayId());
	    wxAppPaySignRequest.setSign(SignUtils.createSign(wxAppPaySignRequest, null,"01eac548fe4b15fb5a3f1ddd265c0122", false));
	    mape.put("ra", wxPayUnifiedOrderRequest);
	    mape.put("rb", obj);
	    mape.put("rc", wxAppPaySignRequest);
	    return APIResult.createSuccess(mape);*/
	    
	    /*
	   
        return APIResult.createSuccess(wxAppPaySignRequest);*/
    }
	
	@RequestMapping(value = "/prePay", method = { RequestMethod.POST, RequestMethod.GET })
    public Object save(@RequestParam(value = "code", required = true)String  code,
            @RequestParam(value = "businessCode", required = true) String businesscode,
            @RequestParam(value = "appId", required = true) String appId,
            @RequestParam(value = "tradeType", required = false) String tradeType,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params) throws Exception {
		
		 WxMaService wxService=wxAppService.getWxMaServiceByAppId(appId);
		 WxConfigBean wxConfigBean=   wxAppService.getWxConfigBean(appId);
		 System.out.println(jsonService.toJson(wxConfigBean));
		
		 WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest=new WxPayUnifiedOrderRequest();
		Map<String, Object> map=hystrixVisuaSqlExampleClient.getByDataBusinessCode(businesscode, params);
		if(map.containsKey("obj")) {
		  List<Map<String, Object>> rows=(List<Map<String, Object>>) map.get("obj");
		  if(rows!=null&&rows.size()==1) {
			  Map<String, Object> row=rows.get(0);
			  wxPayUnifiedOrderRequest.setBody(row.get("body").toString());
			  if(row.get("totalFee") instanceof Double) {
				  Double d=(Double) row.get("totalFee");
				  wxPayUnifiedOrderRequest.setTotalFee(d.intValue());  
			  }else {
				  wxPayUnifiedOrderRequest.setTotalFee((int)row.get("totalFee"));  
			  }
			  wxPayUnifiedOrderRequest.setOutTradeNo((String) row.get("outTradeNo"));//商户订单号需要修改
			  wxPayUnifiedOrderRequest.setAttach((String) row.get("attach"));
		  }else {
			  return APIResult.createInstance("下单失败，找不到对应的订单号！");
		  }
		}else {
			return APIResult.createInstance("下单失败，找不到对应的订单号！sql有误！");
		}
	    WxMaJscode2SessionResult session =null;
        session = wxService.getUserService().getSessionInfo(code);
	    WxPayConfig config=new WxPayConfig();
	    config.setAppId(wxConfigBean.getApp_id());
	    config.setMchKey(wxConfigBean.getSign_key() );
	    config.setMchId(wxConfigBean.getMchid() );
	    wxPayService.setConfig(config);
	    wxPayUnifiedOrderRequest.setMchId(wxConfigBean.getMchid());
        wxPayUnifiedOrderRequest.setAppid(wxConfigBean.getApp_id());
	    wxPayUnifiedOrderRequest.setNonceStr(WxPayUtils.getNonceStr());//随机数
	    if(StringUtils.isEmpty(tradeType)) {
	    	 wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
	    }else {
	    	 wxPayUnifiedOrderRequest.setTradeType(tradeType);
	    }
	    wxPayUnifiedOrderRequest.setSpbillCreateIp(wxConfigBean.getIp());
	    wxPayUnifiedOrderRequest.setNotifyUrl(wxConfigBean.getNotify_url()+"/"+wxConfigBean.getApp_id());
	    wxPayUnifiedOrderRequest.setOpenid(session.getOpenid());
	    WxPayUnifiedOrderResult  obj= wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
	    WxAppPaySignRequest wxAppPaySignRequest=new WxAppPaySignRequest();
	    wxAppPaySignRequest.setAppid(wxConfigBean.getApp_id());
	    wxAppPaySignRequest.setNonceStr(UUID.randomUUID().toString().substring(4));
	    wxAppPaySignRequest.setTimeStamp(System.currentTimeMillis()/1000+"");
	    wxAppPaySignRequest.setPackAge("prepay_id="+obj.getPrepayId());
	    wxAppPaySignRequest.setSignType(WxPayConstants.SignType.MD5);
	    wxAppPaySignRequest.setPaySign(SignUtils.createSign(wxAppPaySignRequest, null, wxConfigBean.getSign_key(), false));
        return APIResult.createSuccess(wxAppPaySignRequest);
    }
	/**
	 * 支付回调处理
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/notify/{appId}", method = { RequestMethod.POST, RequestMethod.GET })
    public Object notify(@PathVariable String appId,HttpServletRequest request) {
            try {
                InputStream in = request.getInputStream();
                String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
                System.out.println(body);
                WxPayOrderNotifyResult result = BaseWxPayResult.fromXML(body, WxPayOrderNotifyResult.class);
                WxPayConfig config=new WxPayConfig();
                WxConfigBean wxConfigBean=wxAppService.getWxConfigBean(appId);
                config.setAppId(wxConfigBean.getApp_id());
                config.setMchKey(wxConfigBean.getSign_key() );
                config.setMchId(wxConfigBean.getMchid());
                wxPayService.setConfig(config);
                System.out.println(jsonService.toJson(result));
                result.checkResult(wxPayService,"MD5", true);
                if("SUCCESS".equals(result.getResultCode())) {
                	hystrixVisuaSqlExampleClient.getByDataBusinessCode(result.getAttach(), 
                			jsonService.toJson(result));
                }else {
                	
                }
                System.out.println("微信支付成功！！");
                //TODO--继续业务操作
                return WxPayNotifyResponse.success("交易成功");
            } catch (WxPayException e) {
                e.printStackTrace();
                return WxPayNotifyResponse.fail(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                return WxPayNotifyResponse.fail(e.getMessage());
            }
    }
	
}
