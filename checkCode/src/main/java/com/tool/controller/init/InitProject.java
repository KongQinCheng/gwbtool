package com.tool.controller.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitProject implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        //项目初始化执行


//        String html = getHtmlByURL("http://stockpage.10jqka.com.cn/603380/", "UTF-8");

//        System.out.println(html);

//        List<StockList> stockList = iStockListServices.getStockList();
//        List<StockList> returnlist = new ArrayList<>();
//        int count = 1;
//        for (int i = 0; i < stockList.size(); i++) {
//            try {

//                iStockInfoDao.delStockInfo( "000001","2019-08-10");
//
//                iStockInfoDao.delStockInfo(stockList.get(i).getStockCode().replaceAll("\t", "") + "","2019-08-10");

//                List<StockInfo> stockInfoListAll = iStockInfoDao.getNewStockListByStockCode(stockList.get(i).getStockCode().replaceAll("\t", "") + "", SortType.ASC.toString(), 1);
//
//                if (stockInfoListAll.get(0).getCci() == 0) {
//                    System.out.println(stockList.get(i).getStockCode());
//                    System.out.println("count=" + count++);
//                }
//                System.out.println("i=" + i );
//
//            } catch (Exception e) {
//                System.out.println(stockList.get(i).getStockCode());
//                System.out.println("i=" + i);
//                continue;
//            } finally {
//            }
//                iStockInfoCciServices.getCciValue("603383",14);
//        }
//


    }
}
