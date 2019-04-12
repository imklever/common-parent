package com.isoftstone.common.api.controller.wixin.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.common.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.isoftstone.common.api.controller.wixin.bean.WxConfigBean;
import com.isoftstone.common.api.controller.wixin.service.WxAppService;
import com.isoftstone.common.api.service.cache.CacheService;
import com.isoftstone.common.api.service.cache.LocalCacheFactory;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;

@Service
public class WxAppServiceImpl implements WxAppService {
    @Autowired
    HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
    public final static ConcurrentHashMap<String, WxConfigBean> WX_CONFIG_MAP = new ConcurrentHashMap<String, WxConfigBean>();
    @Autowired
    JsonService jsonService;
    @Value("${cache.type:javaCache}")
    String cacheType;
    @Autowired
    LocalCacheFactory localCacheFactory;
    
    @Override
    public WxConfigBean getWxConfigBean(String appId) {
        WxConfigBean wxConfigBean=null;
        if(WX_CONFIG_MAP.contains(appId)) {
            return WX_CONFIG_MAP.get(appId);
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("appId", appId);
        Map<String, Object> datamap = hystrixVisuaSqlExampleClient.getByDataBusinessCode("S09001",
                jsonService.toJson(map));
        System.out.println(jsonService.toJson(datamap));
        if (datamap != null && datamap.containsKey(CommonConstants.RETURN_DATA)) {
            List<Map<String, Object>> datalist = (List<Map<String, Object>>) datamap.get(CommonConstants.RETURN_DATA);
            if (datalist.size() == 1) {
                wxConfigBean=jsonService.parseObject(jsonService.toJson(datalist.get(0)), WxConfigBean.class);
                WX_CONFIG_MAP.put(appId, wxConfigBean);
            }
        }
        return wxConfigBean;
    }

    @Override
    public void setConfig(WxMaService wxService, String appId) {
        Map<String, String> map = new HashMap<String, String>();
        CacheService cacheService = localCacheFactory.getCacheService(cacheType);
        WxMaInMemoryConfig wxMaInMemoryConfig = (WxMaInMemoryConfig) cacheService
                .get(CommonConstants.CACHE_TYPE_KEY_WXAPP, appId);
        if (wxMaInMemoryConfig != null) {
            wxService.setWxMaConfig(wxMaInMemoryConfig);
            return;
        }
        WxConfigBean wxConfigBean=getWxConfigBean(appId);
        if(wxConfigBean!=null) {
            WxMaInMemoryConfig config = new WxMaInMemoryConfig();
            config.setAppid(wxConfigBean.getApp_id());
            config.setSecret(wxConfigBean.getSecret() );
            config.setToken(wxConfigBean.getToken()  );
            config.setAesKey(wxConfigBean.getAes_key() );
            config.setMsgDataFormat( wxConfigBean.getMsg_data_format());
            wxService.setWxMaConfig(config);
            cacheService.put(CommonConstants.CACHE_TYPE_KEY_WXAPP, appId, config);
        }
    }
    @Override
    public WxMaMessageRouter getWxMaMessageRouterByAppId(String appId) {
        CacheService cacheService = localCacheFactory.getCacheService(cacheType);
        WxMaMessageRouter wxMaMessageRouter = (WxMaMessageRouter)
                    cacheService.get(CommonConstants.CACHE_TYPE_KEY_WXAPP_ROUTER, appId);
        return wxMaMessageRouter;
    }

    @Override
    public WxMaService getWxMaServiceByAppId(String appId) {
        CacheService cacheService = localCacheFactory.getCacheService(cacheType);
        WxMaService wxService = (WxMaService) cacheService.get(CommonConstants.CACHE_TYPE_KEY_WXAPP, appId);
        if (wxService != null) {
            return wxService;
        }
        WxConfigBean wxConfigBean=getWxConfigBean(appId);
        if(wxConfigBean!=null) {
            WxMaInMemoryConfig config = new WxMaInMemoryConfig();
            config.setAppid(wxConfigBean.getApp_id());
            config.setSecret(wxConfigBean.getSecret() );
            config.setToken(wxConfigBean.getToken()  );
            config.setAesKey(wxConfigBean.getAes_key() );
            config.setMsgDataFormat( wxConfigBean.getMsg_data_format());
            wxService = new WxMaServiceImpl();
            wxService.setWxMaConfig(config);
            cacheService.put(CommonConstants.CACHE_TYPE_KEY_WXAPP, appId, wxService);
            cacheService.put(CommonConstants.CACHE_TYPE_KEY_WXAPP_ROUTER, appId, appNewRouter(wxService));
        }
        return wxService;
    }

    public WxMaMessageRouter appNewRouter(WxMaService service) {
        final WxMaMessageRouter router = new WxMaMessageRouter(service);
        router.rule().handler(logHandler).next().rule().async(false).content("模板").handler(templateMsgHandler).end()
                .rule().async(false).content("文本").handler(textHandler).end().rule().async(false).content("图片")
                .handler(picHandler).end().rule().async(false).content("二维码").handler(qrcodeHandler).end();
        return router;
    }

    private static final WxMaMessageHandler templateMsgHandler = (wxMessage, context, service,
            sessionManager) -> service.getMsgService()
                    .sendTemplateMsg(WxMaTemplateMessage.builder().templateId("此处更换为自己的模板id").formId("自己替换可用的formid")
                            .data(Lists.newArrayList(new WxMaTemplateMessage.Data("keyword1", "339208499", "#173177")))
                            .toUser(wxMessage.getFromUser()).build());

    private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("收到信息为：" + wxMessage.toJson())
                .toUser(wxMessage.getFromUser()).build());
    };

    private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> service
            .getMsgService()
            .sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("回复文本消息").toUser(wxMessage.getFromUser()).build());

    private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            WxMpMessageRouter a = null;
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", "png",
                    ClassLoader.getSystemResourceAsStream("tmp.png"));
            service.getMsgService().sendKefuMsg(WxMaKefuMessage.newImageBuilder().mediaId(uploadResult.getMediaId())
                    .toUser(wxMessage.getFromUser()).build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    };

    private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            final File file = service.getQrcodeService().createQrcode("123", 430);
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
            service.getMsgService().sendKefuMsg(WxMaKefuMessage.newImageBuilder().mediaId(uploadResult.getMediaId())
                    .toUser(wxMessage.getFromUser()).build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    };

   

    

}
