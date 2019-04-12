package com.isoftstone.common.networm.util;


public class Constant {
    /**新浪财经公司列表请求url
     * @param 页数，行业
     * @return
     */
    public static String xlcjCompanyUrl(int page ,String hy){
        return "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page="
                + page + "&num=100&sort=symbol&asc=1&node=" +  hy + "&symbol=&sra=init";
    }

    /**
     * 新浪财经公司列表个数请求url
     * @param 行业
     * @return
     */
    public static String xlcjCompanyCountUrl (String hy){
        return "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeStockCount?node=" + hy;
    }
    //新浪财经行情列表请求url
    public static String xlcjHqListUrl = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodes";

    /**
     * 公司详情页面请求url
     * @param 公司代码
     * @return
     */
    public static String companyDetialInfoUrl(String code){
        return "http://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpInfo/stockid/"+code+".phtml";
    }

    /**
     * 公司历史年份请求url
     */
    public static String companyHistoryYearUrl(String code){
        return "http://money.finance.sina.com.cn/corp/go.php/vFD_FinancialGuideLine/stockid/"+code+"/ctrl/displaytype/4.phtml";
    }

    /**
     * 公司某年份请求url
     */
    public static String companyYearUrl(String code,String year){
        return "http://money.finance.sina.com.cn/corp/go.php/vFD_FinancialGuideLine/stockid/"+code+"/ctrl/"+year+"/displaytype/4.phtml";
    }


    /**
     * 财务基本请求接口
     * @param 公司代码带字母
     * @return
     */
    public static String FinanceInfoUrl(int type,String code){
        String url = "";
        switch (type){
            case 0 :
                url="http://dcfm.eastmoney.com/em_mutisvcexpandinterface/api/js/get?type=CWBB_LRB20&token=70f12f2f4f091e459a279469fe49eca5&filter=(scode="+code+")&st=reportdate&sr=-1&p=1&ps=100&js={pages:(tp),data:(x),font:(font)}";break;
            case 1 :
                url="http://dcfm.eastmoney.com/em_mutisvcexpandinterface/api/js/get?type=CWBB_ZCFZB20&token=70f12f2f4f091e459a279469fe49eca5&filter=(scode="+code+")&st=reportdate&sr=-1&p=1&ps=100&js={pages:(tp),data:(x),font:(font)}";break;
            case 2 :
                url="http://dcfm.eastmoney.com/em_mutisvcexpandinterface/api/js/get?type=YJBB21_YJBB&token=70f12f2f4f091e459a279469fe49eca5&filter=(scode=" +code+")&st=reportdate&sr=-1&p=1&ps=100&js={pages:(tp),data:(x),font:(font)}";break;
            default:
        }
        return url;
    }

    /**
     * 新浪相关资讯请求url
     * @param 搜索主题
     * @return
     */
    public static String XlRelatedInfoUrl(String theme){
        return "https://search.sina.com.cn/?ac=product&from=tech_index&source=tech&range=title&f_name=&col=&c=news&ie=utf-8&c=news&q="+ theme;
    }

    public static String ofWeekRelatedInfoUrl(String theme){
        return "http://www.ofweek.com/newquery.action?type=1&Sequence=1&keywords="+ theme;
    }
}
