package com.isoftstone.common.api.controller.wixin.app.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.isoftstone.common.api.controller.wixin.bean.WxConfigBean;
import com.isoftstone.common.api.controller.wixin.service.WxAppService;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.util.JsonService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.common.constant.ApiMapperUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(ApiMapperUrlConstants.WX_APP_USER)
public class WxUserController {
    @Autowired
    WxAppService wxAppService;
    @Autowired
    JsonService jsonService;
    @RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
    public Object login(@RequestParam(value = "code", required = true)String  code,
            @RequestParam(value = "appId", required = true)String  appId) {
        WxMaJscode2SessionResult session =null;
        try {
        	 WxMaService wxService=null;
        	 WxConfigBean wxConfigBean=wxAppService.getWxConfigBean(appId);
             System.out.println(jsonService.toJson(wxConfigBean));
        	/*if(appId!=null&&appId.length()>0) {
        	     appId="wx58a78ead17454375";
        	 }*/
        	 wxService=wxAppService.getWxMaServiceByAppId(appId);
             session = wxService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return APIResult.createSuccess(session);
    }
}
