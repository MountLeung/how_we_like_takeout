package com.howwelike.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自定义定时任务类
 */
@Component
@Slf4j
public class MyTask {

    /**
     * 定时任务
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    public void executeTask(){
        log.info("开始执行定时任务：{}", new Date());
    }
}
