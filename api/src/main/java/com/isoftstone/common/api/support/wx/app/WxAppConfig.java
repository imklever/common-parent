package com.isoftstone.common.api.support.wx.app;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.google.common.collect.Lists;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.File;
@Configuration
public class WxAppConfig extends WebMvcConfigurationSupport{
    private static final WxMaMessageHandler templateMsgHandler = (wxMessage, context, service, sessionManager) ->
    service.getMsgService().sendTemplateMsg(WxMaTemplateMessage.builder()
            .templateId("此处更换为自己的模板id")
            .formId("自己替换可用的formid")
            .data(Lists.newArrayList(
                    new WxMaTemplateMessage.Data("keyword1", "339208499", "#173177")))
            .toUser(wxMessage.getFromUser())
            .build());

        private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("收到信息为：" + wxMessage.toJson())
                .toUser(wxMessage.getFromUser()).build());
        };
        
        private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) ->
            service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("回复文本消息")
                    .toUser(wxMessage.getFromUser()).build());
    
   // @Bean
    public WxMaMessageRouter router(WxMaService service) {
        final WxMaMessageRouter router = new WxMaMessageRouter(service);
        router
                .rule().handler(logHandler).next()
                .rule().async(false).content("模板").handler(templateMsgHandler).end()
                .rule().async(false).content("文本").handler(textHandler).end()
                .rule().async(false).content("图片").handler(picHandler).end()
                .rule().async(false).content("二维码").handler(qrcodeHandler).end();
        return router;
    }
    private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            WxMpMessageRouter a=null;
            WxMediaUploadResult uploadResult = service.getMediaService()
                    .uploadMedia("image", "png",
                            ClassLoader.getSystemResourceAsStream("tmp.png"));
            service.getMsgService().sendKefuMsg(
                    WxMaKefuMessage
                            .newImageBuilder()
                            .mediaId(uploadResult.getMediaId())
                            .toUser(wxMessage.getFromUser())
                            .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    };

    private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            final File file = service.getQrcodeService().createQrcode("123", 430);
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
            service.getMsgService().sendKefuMsg(
                    WxMaKefuMessage.newImageBuilder()
                            .mediaId(uploadResult.getMediaId()).toUser(wxMessage.getFromUser())
                            .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    };
   /* @Bean
    @ConditionalOnMissingBean
    public WxMaConfig maConfig() {
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
       
        * 神驼情小程序
        *  config.setAppid("wxf8c5ee0b29f0fb65");//wxf8c5ee0b29f0fb65 --小程序
        //config.setAppid("wxca43ff8d1389b583");
        config.setSecret("2918b847e119f5cb962556efa0a731d2");
        config.setToken("shentuoqing@163.com");
        config.setAesKey("Kggmf4D5MCUBN6H4fV8vhgIYwZw2uHpZf0m9pgGAZb7");
        config.setMsgDataFormat("JSON");
        
        
        
        
        
        config.setAppid("wx58a78ead17454375");
        config.setSecret("ac069966def160aac553dd00964f9f27");
        config.setToken("shentuoqing@163.com");
        config.setAesKey("Kggmf4D5MCUBN6H4fV8vhgIYwZw2uHpZf0m9pgGAZb7");
        config.setMsgDataFormat("JSON");
        return config;
    }
    @Bean
    @ConditionalOnMissingBean
    public WxMaService wxMaService(WxMaConfig maConfig) {
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(maConfig);
        return service;
    }*/
}
