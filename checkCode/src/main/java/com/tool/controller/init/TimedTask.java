package com.tool.controller.init;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class TimedTask {


    @Scheduled(cron = "0 0 16 * * ?")
    public void getStockInfo() throws Exception {
        //获取每一只最新的股票信息
    }

}
