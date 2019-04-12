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
//import com.isoftstone.common.networm.service.EnterpriseinfoSpliderService;
//import com.isoftstone.common.networm.util.ExcelUtil;
//import com.isoftstone.common.util.JsonService;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ServiceNetwormApplicationTests {
//
//	@Autowired
//	EnterpriseinfoSpliderService enterpriseinfoSpliderService;
//	@Autowired
//	JsonService jsonService;
//
//	@Test
//	public void contextLoads() {
//		String sheetName = "公司信息";
//	    int columnNumber = 7;
//		int[] columnWidth = { 10, 20, 30,50,50,50,100};
//		String[] columnName = { "代码","公司名称", "证券类型", "上市交易所","公司地址","注册地址","经验范围"};
//
//		String node ="sw_dz";
//		//sw_tx
//		//sw_jsj
//		//sw_dz
//		String fileName = "电子行业_公司信息";//"计算机行业_公司信息";
//		List<List<String>> dataList=new ArrayList<List<String>>();
//		int pageIndex = 1;
//		while(true){
//			//获取上市公司的股票代码与公司名称
//			String body = enterpriseinfoSpliderService.getData("http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page="+pageIndex+"&num=80&sort=symbol&asc=1&node="+node+"&symbol=&_s_r_a=page");
//
//			if(body==null || StringUtils.isEmpty(body)|| "null".equals(body.trim())){
//			   break;
//			}
//
//			System.out.println("body: "+body);
//
//			List<Map<String, String>> enterpriseList=enterpriseinfoSpliderService.getList(body);
//			System.out.println("content: ");
//		    for (Map<String, String> map : enterpriseList) {
//		    	System.out.println(" { symbol:"+map.get("symbol").toString()+"; name:"+map.get("name").toString()+"}");
//		    	String symbol = map.get("symbol").toString();
//		        //获取上市公司的详细信息
//			    String enterpriseBody = enterpriseinfoSpliderService.getData("http://emweb.securities.eastmoney.com/PC_HSF10/CompanySurvey/CompanySurveyAjax?code="+symbol);
//			    Map<String, Object> resultData=jsonService.parseMap(enterpriseBody);
//			    List<String> gsDetails=new ArrayList<String>();
//			    Map<String, Object> result = (Map<String, Object>)resultData.get("Result");
//			    Map<String, Object> jbzl =(Map<String, Object>) result.get("jbzl");
//
//			    gsDetails.add(jbzl.get("agdm").toString());
//		    	gsDetails.add(jbzl.get("gsmc").toString());
//		    	gsDetails.add(jbzl.get("zqlb").toString());
//		    	gsDetails.add(jbzl.get("ssjys").toString());
//		    	gsDetails.add(jbzl.get("bgdz").toString());
//		    	gsDetails.add(jbzl.get("zcdz").toString());
//		    	gsDetails.add(jbzl.get("jyfw").toString());
//		    	dataList.add(gsDetails);
//			}
//
//		    try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		     pageIndex++;
//		}
//
//	    try {
//			ExcelUtil.ExportExcelData(sheetName, fileName, columnNumber, columnWidth, columnName, dataList);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//	}
//
//}
