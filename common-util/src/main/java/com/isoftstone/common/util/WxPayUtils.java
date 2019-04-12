package com.isoftstone.common.util;

import java.util.UUID;

public class WxPayUtils {
    /**
     * 获取32为随机数
     * @return
     */
    public static String getNonceStr() {
        return UUID.randomUUID().toString().substring(4);
    }

}
