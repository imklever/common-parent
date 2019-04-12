package com.isoftstone.common.api.controller.wixin.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.isoftstone.common.api.controller.wixin.bean.WxConfigBean;

public interface WxAppService {

	void setConfig(WxMaService wxService, String appId);

	WxMaService getWxMaServiceByAppId(String appId);
	
	WxMaMessageRouter getWxMaMessageRouterByAppId(String appId);
	
	WxConfigBean  getWxConfigBean(String appId);

}
