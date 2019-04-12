package com.isoftstone.common.networm.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class AddressResolutionUtil {
	
	/**
     * 解析地址
     * @author lin
     * @param address
     * @return
     */
    public static List<Map<String,String>> addressResolution(String address){
       // String regex="(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        String regex="(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?行政单位|.+盟|市辖区|.*?市|.*?区|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
    	Matcher m=Pattern.compile(regex).matcher(address);
        String province=null,city=null,county=null,town=null,village=null;
        List<Map<String,String>> table=new ArrayList<Map<String,String>>();
        Map<String,String> row=null;
        while(m.find()){
            row=new LinkedHashMap<String,String>();
            province=m.group("province");
            row.put("province", province==null?"":province.trim());
            city=m.group("city");
            row.put("city", city==null?"":city.trim());
            county=m.group("county");
            row.put("county", county==null?"":county.trim());
            town=m.group("town");
            row.put("town", town==null?"":town.trim());
            village=m.group("village");
            row.put("village", village==null?"":village.trim());
            table.add(row);
        }
        return table;
    }
 
	public static void main(String[] args) {
	/*	System.out.println(addressResolution("湖北省武汉市洪山区"));
		System.out.println(addressResolution("北京市西城区金融大街21号"));
		System.out.println(addressResolution("湖北省武汉市东湖开发区关东工业园文华路2号"));
		System.out.println(addressResolution("北京市海淀区杏石口路99号B座"));*/
		System.out.println(addressResolution("湖北省公安县斗湖堤镇城关"));
		System.out.println(addressResolution("山东省昌邑县"));
		
		/*
		 
		[{province=湖北省, city=武汉市, county=洪山区, town=, village=}]
		[{province=北京市, city=西城区, county=, town=, village=金融大街21号}]
		[{province=湖北省, city=武汉市, county=东湖开发区, town=, village=关东工业园文华路2号}]
		[{province=北京市, city=海淀区, county=, town=, village=杏石口路99号B座}]
		[{province=湖北省, city=公安县, county=, town=斗湖堤镇, village=城关}]
		[{province=山东省, city=临朐县, county=, town=, village=张家庄子村}]
		*/
		
	}
 
}

