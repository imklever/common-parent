package com.isoftstone.common.backup.controller.common.sys;

import com.isoftstone.common.backup.service.plugins.visua.VisuaSqlExampleService;
import com.isoftstone.common.backup.service.sms.ALiSmsService;
import com.isoftstone.common.backup.service.sms.JDMsgService;
import com.isoftstone.common.backup.service.sms.ZsyMsgService;
import com.isoftstone.common.common.sys.SysSmsServiceDefinition;
import com.isoftstone.common.service.CommonSqlService;
import org.common.constant.ServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RefreshScope
@RestController
@RequestMapping(name = ServiceBackConstants.BACK_SERVICE,
        path = ServiceBackConstants.PATH_SYS_USER)
public class SysSmsController implements SysSmsServiceDefinition {
    @Autowired
    CommonSqlService commonSqlService;
    @Autowired
    VisuaSqlExampleService visuaSqlExampleService;
    @Autowired
    JDMsgService jdMsgService;
    @Autowired
    ALiSmsService aliSmsService;
    @Autowired
    ZsyMsgService zsySmsService;
    public static String appId = "LTAIjRdNjG69qteG";
    public static String appKey = "rA3OXxEyjOGNSgBPSRrXLofyRHxvFX";
    public static String codeTemplate = "SMS_122294272";
    public static String signName = "创客智能管家";

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public void sendMessage(Map<String, Object> datas) {
        System.out.println("准备发送短信");
        if (datas != null && datas.containsKey("msgdataList")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) datas.get("msgdataList");
            for (Map<String, Object> string : list) {
                System.out.println("id为" + string.get("id"));
//				 Map<String, Object> datasMap = new HashMap<String, Object>();
//				 datasMap.put("id", string);
                String sql = "SELECT * FROM sys_sms_info a \r\n" +
                        "LEFT JOIN sys_sms_config_table b\r\n" +
                        "ON a.`sms_key`=b.`sms_key`\r\n" +
                        "LEFT JOIN sys_sms_templ c\r\n" +
                        "ON a.`codeTemplate` = c.codeTemplate\r\n" +
                        "WHERE a.id =";
                List<Map<String, Object>> sqlQuery = commonSqlService.getSqlQuery(sql + string.get("id"), null, null);
                for (Map<String, Object> map : sqlQuery) {
                    String sms_type = map.get("sms_type").toString();
                    String phone = map.get("phone").toString();
                    String content = map.get("content").toString();
                    String appId = map.get("appId").toString();
                    String appKey = map.get("appKey").toString();
                    String signName = map.getOrDefault("signName", "signName").toString();
                    String userId = map.getOrDefault("userId", "userId").toString();
                    String sms_key = map.getOrDefault("sms_key", "sms_key").toString();
                    try {
                        if (sms_type.equals("1")) {
                            jdMsgService.test(appId, appKey, sms_key, signName, userId, phone, content);
                        } else if (sms_type.equals("2")) {
                            String codeTemplate = map.get("codeTemplate").toString();
                            aliSmsService.sendSmsCode(phone, content, signName, codeTemplate, appId, appKey);
                        } else if (sms_type.equals("3")) {
                            zsySmsService.test(appId, appKey, sms_key, signName, userId, phone, content);
                        }
                    } catch (Exception e) {
                    }
                }
                //更新发送状态
                visuaSqlExampleService.getByDataBusinessCode("U03010", "{id:\"" + string.get("id") + " \"}");
            }
        }

//		return null;

    }

    @SuppressWarnings("test-unlikely-arg-type")
    @Override
    public void testSendMessage(Map<String, Object> datas) {
        System.out.println("准备发送短信");

        aliSmsService.sendSmsCode("15910294794", "{\"code\":\"" + 123321 + "\"}", signName, codeTemplate, appId, appKey);
    }
}
