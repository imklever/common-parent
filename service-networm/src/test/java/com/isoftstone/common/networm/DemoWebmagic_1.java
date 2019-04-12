package com.isoftstone.common.networm;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class DemoWebmagic_1 implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
     /*   page.addTargetRequests(page.getHtml().links().regex("http://vip.stock.finance.sina.com.cn/mkt/#sw_tx").all());
        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-])").all());
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name")==null){
            //skip this page
            page.setSkip(true);
        }
        
        System.out.println("-------------------------------------------");
        System.out.println("readme:"+page.getHtml().xpath("//div[@id='readme']/tidyText()"));
        
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));*/
        
    	//page.addTargetRequests(page.getHtml().links().regex("http://vip.stock.finance.sina.com.cn/mkt/#sw_tx").all());
    	
    	List<String> list = page.getHtml().xpath("//div[@id='con02-1']/table[@id='BalanceSheetNewTable0']/tbody/tr[@class!='gray']/td/tidyText()").all();
    	
    	int i=0;
    	for (String str : list) {
			if(str.contains("主营业务利润率(%)")){
				System.out.println("主营业务利润率(%):" + list.get(i+1));
			}
			i++;
		}
    	
    	/* 财务指标 */
        System.out.println("tbl_wrap:------\n" + page.getHtml().xpath("//div[@id='con02-1']/table[@id='BalanceSheetNewTable0']/tbody/tr[@class!='gray']/td/tidyText()").all());
    	
        //page.putField("readme", page.getHtml().xpath("//div[@id='tbl_wrap']/text()"));
    	 
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {  
        Spider.create(new DemoWebmagic_1()).addUrl("http://vip.stock.finance.sina.com.cn/corp/go.php/vFD_FinancialGuideLine/stockid/600050/displaytype/4.phtml").thread(5).run();
    }

}