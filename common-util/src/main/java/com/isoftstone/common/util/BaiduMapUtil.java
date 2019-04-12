package com.isoftstone.common.util;

import java.util.List;

public  class BaiduMapUtil {
    /**
     * 经纬度计算2点之距离，返回km
     * @param latLngs
     * @return
     */
    public static  double getDistance(List<double[]> latLngs) {
        double distance = 0.0;
        if (latLngs.size() >= 2) {
            for (int i = 1; i < latLngs.size(); i++) {
               // double R = 6371.393;//6378.137; // 地球半径
                double R = 6378.137;
                double lat1 = latLngs.get(i-1)[1] * Math.PI / 180.0;
                double lng1 = latLngs.get(i-1)[0] * Math.PI / 180.0;
                double lat2 = latLngs.get(i)[1] * Math.PI / 180.0;
                double lng2 = latLngs.get(i)[0] * Math.PI / 180.0;
                double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+
                        Math.cos(lat1)*Math.cos(lat2)*Math.cos(lng2-lng1))*R;
                d = (double)Math.round(d*100000)/100000;
                distance += d;
            }
        }
        distance = (double)Math.round(distance*100000)/100000*1000;
        return distance;
    }
}
