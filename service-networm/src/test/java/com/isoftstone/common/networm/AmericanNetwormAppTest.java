//package com.isoftstone.common.networm;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.StringUtils;
//
//import com.alibaba.fastjson.JSONArray;
//import com.isoftstone.common.networm.service.EnterpriseinfoSpliderService;
//import com.isoftstone.common.networm.util.ExcelUtil;
//import com.isoftstone.common.util.JsonService;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class AmericanNetwormAppTest {
//
//	@Autowired
//	EnterpriseinfoSpliderService enterpriseinfoSpliderService;
//	@Autowired
//	JsonService jsonService;
//
//	@Test
//	public void test() {
//
//
//		String sheetName = "公司信息";
//	    int columnNumber = 7;
//		int[] columnWidth = { 10, 20, 30,50,50,50,100};
//		String[] columnName = { "代码","公司名称", "证券类型", "上市交易所","公司地址","注册地址","经验范围"};
//
//		String fileName = "美股_公司信息";//"计算机行业_公司信息";
//		List<List<String>> dataList=new ArrayList<List<String>>();
//		int pageIndex = 1;
//		while(true){
//
//			//http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?cb=jQuery1124093862860861858_1540456947363&type=CT&token=4f1862fc3b5e77c150a2b985b12db0fd&sty=FCOIATC&js=(%7Bdata%3A%5B(x)%5D%2CrecordsFiltered%3A(tot)%7D)&cmd=C.MK0216&st=(ChangePercent)&sr=-1&p=1&ps=20&_=1540456947364
//			String body = enterpriseinfoSpliderService.getData("http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&token=4f1862fc3b5e77c150a2b985b12db0fd&sty=FCOIATC&js=(%7Bdata%3A%5B(x)%5D%2CrecordsFiltered%3A(tot)%7D)&cmd=C.MK0216&st=(ChangePercent)&sr=-1&p="+pageIndex+"&ps=20&_=1540462557603");
//
//			body=body.replace("%7B", "{").replace("%3A", ":").replace("%5B", "[").replace("%5D", "]").replace("%2C", ",")
//			.replace("%7D", "}");
//
//			int startIndex = body.indexOf("(")+1;
//			String jsonStr =body.substring(startIndex,body.length()-1);
//			Map<String, Object> dataMap = jsonService.parseMap(jsonStr);
//			JSONArray jsonArray=(JSONArray) dataMap.get("data");
//
//			for (int i=0;i<jsonArray.size();i++) {
//				String[] rowData = jsonArray.get(i).toString().split(",");
//				String code =rowData[1]+".N";
//				String enterpriseBody = enterpriseinfoSpliderService.getData("http://emweb.eastmoney.com/pc_usf10/CompanyInfo/PageAjax?fullCode="+code);
//
//				System.out.print("content:code:"+rowData[1]+"; name:"+rowData[2]);
//
//				 Map<String, Object> resultData=jsonService.parseMap(enterpriseBody);
//			    List<String> gsDetails=new ArrayList<String>();
//			    Map<String, Object> result = (Map<String, Object>)resultData.get("data");
//			    List<Map<String, Object>> jbzl =(List<Map<String, Object>>) result.get("gszl");
//			    for (Map<String, Object> map : jbzl) {
//			    	//{ "代码","公司名称", "证券类型", "上市交易所","公司地址","注册地址","经验范围"};
//					gsDetails.add(map.get("SECURITYCODE").toString());
//
//					String companyName=map.get("COMPNAMECN").toString();
//					if(!StringUtils.isEmpty(map.get("COMPNAMECN").toString())&& "--".equals(map.get("COMPNAMECN").toString())){
//						companyName=map.get("COMPNAMECN").toString();
//					}
//
//			    	gsDetails.add(companyName);
//			    	gsDetails.add("");
//			    	gsDetails.add("");
//			    	gsDetails.add(map.get("OFFICEADDRESS").toString());
//			    	gsDetails.add(map.get("ADDRESS").toString());
//			    	gsDetails.add("");
//			    	dataList.add(gsDetails);
//				}
//			}
//
//		    try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		     pageIndex++;
//
//		     if(pageIndex>2){
//
//		     }
//		     try {
//				ExcelUtil.ExportExcelData(sheetName, fileName, columnNumber, columnWidth, columnName, dataList);
//			 } catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		}
//
//	}
//
//}
