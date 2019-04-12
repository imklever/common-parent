package com.isoftstone.common.api.controller.wixin.motion;

import cn.binarywang.wx.miniapp.util.crypt.WxMaCryptUtils;
import com.alibaba.fastjson.JSONObject;
import com.isoftstone.common.api.controller.wixin.service.WxAppService;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;
import org.common.constant.ApiMapperUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
 
@RestController
@RequestMapping(ApiMapperUrlConstants.WX_APP_MOTION)
public class WxMotionController {
  /*  @Autowired
    private WxMaService wxService;*/
    @Autowired
    WxAppService wxAppService;
    @Autowired
    private JsonService jsonService;
    @Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
     
    /** 
     * 解密用户敏感数据 
     * 
     * @param encryptedData 明文,加密数据 
     * @param iv      加密算法的初始向量 
     * @param code     用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，
     * 				使用code 换取 session_key api，将 code 换成 openid 和 session_key 
     * @return 
     */
    @RequestMapping(value = "/decodeMition", method = { RequestMethod.POST, RequestMethod.GET })
    public Object decodeUser(String encryptedData,
    		String openid,
    		String iv, 
    		String sessionKey,
    		@RequestParam(value = "code", required = false)String code,
    		@RequestParam(value = "businessCode", required = false)String businessCode,
    	    @RequestParam(value = "appId", required = false)String appId,
    	    @RequestParam(value = "parms", required = false)String parms) { 
      String result = null;
       try { 
    	    result= WxMaCryptUtils.decrypt(sessionKey, encryptedData, iv);
    		JSONObject obj= (JSONObject) jsonService.parseObject(result);
    		obj.put("openid", openid);
    		System.out.println(obj.toJSONString());
    		if(!StringUtils.isEmpty(businessCode)) {
    			Map<String, Object> map= hystrixVisuaSqlExampleClient.getByDataBusinessCode(businessCode,obj.toJSONString() );
    			System.out.println(jsonService.toJson(map));
    		}
      } catch (Exception e) { 
    	   e.printStackTrace(); 
      } 
       return APIResult.createSuccess(result);
    } 
  
    
    public static void main(String[] args) {
    	 Object result = null;
         try { 
        	 
        	 String encryptedData="QSZKMbLHWcFbKFNdyu9kTjobxXpVhu9ZKSxRxreKua/RenNHNkRC6GMmg3W3Q6VgAiR4vlOkC+jQNHM+kuaVGaTFRpB3FhaK9K6yGjUw70BefqOpbHdj/HVroo4favGbbmv3CBrASQzx9MeLuQE4W5H454fPbs48adaI2ztmdEqRvOiTrdlo+qKCBfNGPw30goBAb2+lL62iY91v0ehI80xRB32xFvAmT8LwGb9w+0wi3orx4tqmYO4wAZ3TQ+LpaiBiMzOM2X60+zjk/p7nrJATUjX1Rw6SfOKBsRvMoC51Ml+1RMl38jh+hceM9Efn6NMCwWnZxklYcHCQOMPZeiztV23IeBuzfnpUr2sOqI2mW8zi3pq7QFLSN7VzY4T9rvOvdQdQasjAeDJOTQt0m3nOCX9xaqz8tnYoUJ9r0//RAkKG2rkly/LzowNmn4YDfEjmCO7BXgsj/T+XWpg/rPQn8eXNpAhFg4nsoqgpX79PTs9aA5ZZZv9S32FnYS4cfaM7hPL9jhoTB1nCnwVMkthf6wf89E5wUBxgqS4suq8=";
        	 String sessionKey="tK7Ffc7uVnAAMqtjSD7usg==";
        	 String            iv="atVQ3Or74LXwXjVxbj5uRw==";
        	 
        	 
        	 result= WxMaCryptUtils.decrypt(sessionKey, encryptedData, iv);
        	 
          
          System.out.println(result);
        } catch (Exception e) { 
      	   e.printStackTrace(); 
        } 
	}
}
