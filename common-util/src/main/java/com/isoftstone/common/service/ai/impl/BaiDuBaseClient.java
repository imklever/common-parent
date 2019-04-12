package com.isoftstone.common.service.ai.impl;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import com.baidu.aip.client.BaseClient;
import com.baidu.aip.error.AipError;
import com.baidu.aip.http.AipRequest;
import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;

public class BaiDuBaseClient extends BaseClient{

	protected BaiDuBaseClient(String appId, String apiKey, String secretKey) {
		super(appId, apiKey, secretKey);
	}
	public JSONObject basicGeneral(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }        
        request.setUri("https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic");
        postOperation(request);
        return requestServer(request);
    }
    
    public JSONObject handwriting(String image, HashMap<String, String> options) {
        try {
            byte[] imgData = Util.readFileByBytes(image);
            return basicGeneral(imgData, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }
}
