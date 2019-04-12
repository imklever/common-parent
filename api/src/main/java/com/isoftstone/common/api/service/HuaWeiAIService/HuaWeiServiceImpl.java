package com.isoftstone.common.api.service.HuaWeiAIService;

import com.alibaba.fastjson.JSON;
import com.cloud.sdk.http.HttpMethodName;
import com.isoftstone.common.api.support.websocket.WebSocketServer;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HuaWeiServiceImpl {

    @Autowired
    WebSocketServer webSocketServer;

    private static final String region = "cn-north-1";
    private static final String serviceName = "cbs";
    private static final String ak = "ESJAB6OOETVOQFVWUAKZ";
    private static final String sk = "KkSmZhWGeGuUiIflEMN1cswEmJ2e7ivIRpyDrpL6";
    private static final String projectId = "12b3d621b74d491589b945f2486a0845";
    private static final String robotId = "795cc94a-fcb6-4cb2-95ad-6566a159bae3";

    public Map<String,Object> postAskMethod(String message,String sessionId){
        String url = "https://cbs.cn-north-1.myhuaweicloud.com/v1/"+projectId+"/qabots/"+robotId+"/requests";
        Map map = JSONObject.fromObject(message);
        String msg = map.get("msg").toString();
        int type = Integer.parseInt(map.get("msgType").toString());
        String postBody = "{\n" +
                            "    \"session_id\": \""+sessionId+"\",\n" +
                            "    \"operate_type\":"+type+",\n" +
                            "    \"question\": \""+msg+"\"\n" +
                            "}";
        return postAsk(url,postBody);
    }

    public List<String> postQuestionTips(String message,String sessionId){
        String url = "https://cbs.cn-north-1.myhuaweicloud.com/v1/"+projectId+"/qabots/"+robotId+"/suggestions";
        String postBody = "{\n" +
                            "    \"question\":\""+message+"\",\n" +
                            "    \"top\":5\n" +
                            "}";
        return postTips(url,postBody);
    }

    public void postMYDegree(String message,String sessionId){
        Map map = JSONObject.fromObject(message);
        String requestId = map.get("requestId").toString();
        int num = Integer.parseInt(map.get("myd").toString());
        String url = "https://cbs.cn-north-1.myhuaweicloud.com/v1/"+projectId+"/qabots/"
                +robotId+"/requests/"+requestId+"/satisfaction";
        String postBody = "{\n" +
                            "    \"degree\":"+num+"\n" +
                            "}";
        postDegree(url,postBody);

    }

    /**
     * 满意度评价
     * @param requestUrl
     * @param postbody
     */
    public void postDegree(String requestUrl, String postbody){
        AccessService accessService = new AccessServiceImpl(serviceName, region, ak, sk);
        URL url = null;
        String msg = "";
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream content = new ByteArrayInputStream(postbody.getBytes());
        HttpMethodName httpMethod = HttpMethodName.POST;
        HttpResponse response;
        try {
            response = accessService.access(url, content, (long) postbody.getBytes().length, httpMethod);
            msg = convertStreamToString(response.getEntity().getContent()).toString();
            System.out.println(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            accessService.close();
        }
    }

    /**
     * 问题提示请求
     * @param requestUrl
     * @param postbody
     * @return
     */
    public List<String> postTips(String requestUrl, String postbody){
        AccessService accessService = new AccessServiceImpl(serviceName, region, ak, sk);
        URL url = null;
        List<String> list = new ArrayList<>();
        String msg = "";
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream content = new ByteArrayInputStream(postbody.getBytes());
        HttpMethodName httpMethod = HttpMethodName.POST;
        HttpResponse response;
        try {
            response = accessService.access(url, content, (long) postbody.getBytes().length, httpMethod);
            msg = convertStreamToString(response.getEntity().getContent()).toString();
            System.out.println(msg);
            Map maps = (Map) JSON.parse(msg.replace("\n",""));
            String questions = maps.get("questions").toString();
            if(!"[]".equals(questions)){
                list = (List<String>) JSON.parse(questions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            accessService.close();
        }
        return list;
    }

    /**
     * 问答请求方法
     * @param requestUrl
     * @param postbody
     * @return
     */
    public Map<String,Object> postAsk(String requestUrl, String postbody){
        AccessService accessService = new AccessServiceImpl(serviceName, region, ak, sk);
        URL url = null;
        String msg = "";
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream content = new ByteArrayInputStream(postbody.getBytes());
        HttpMethodName httpMethod = HttpMethodName.POST;
        HttpResponse response;
        Map<String,Object> map = new HashMap<>();
        try {
            response = accessService.access(url, content, (long) postbody.getBytes().length, httpMethod);
            msg = convertStreamToString(response.getEntity().getContent()).toString();
            System.out.println(postbody);
            System.out.println(msg);
            Map maps = (Map) JSON.parse(msg.replace("\n",""));
            String title = "";
            String request_id = "";
            List<String> tuijianQuestList = new ArrayList<>();
            msg = "";
            if (maps.get("error_code") == null){
                String answers = maps.get("answers").toString();
                if(!"[]".equals(answers)){
//                    msg = "不好意思，我不是很理解您的问题，请您换一种方式描述您的问题，谢谢。"
                    List<Map> list = (List<Map>) JSON.parse(answers);
                    for(Map m : list){
                        if(m.get("answer") != null) {
                            title = m.get("st_question").toString();
                            msg = m.get("answer").toString();
                        }
                        tuijianQuestList.add(m.get("st_question").toString());
                    }
                }else{
                    msg="";
                }
                request_id = maps.get("request_id").toString();
            }else{
                msg = "账号认证失败，请检查。";
            }
            map.put("request_id",request_id);
            map.put("title",title);
            map.put("questionsList",tuijianQuestList);
            map.put("msg",msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            accessService.close();
        }
        return map;
    }

    private static String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        HuaWeiServiceImpl huaWeiServiceImpl = new HuaWeiServiceImpl();
//        Map map = new HashMap();
//        map.put("requestId","233412f7-95ca-4709-9633-d601ef25ed2e1548665364589");
//        map.put("myd",1);
        huaWeiServiceImpl.postQuestionTips("ce","1");
    }
}
