package com.yjtse.dto;//package com.yjtse.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

//public class MyJob implements Job {
public class Job1 extends QuartzJobBean {

//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        System.out.println(LocalDateTime.now() + ": job 1 doing something...");
//    }
//
//    @Autowired
//    private SocketDao socketDao;
//
//    @Autowired
//    private QuartzManager quartzManager;

    private int timeout;
    private static int i = 0;

    //调度工厂实例化后，经过timeout时间开始执行调度
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }



    /**
     * 要调度的具体任务
     */

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        获取要修改的状态的插座的id
//        String socketId = jobExecutionContext.getMergedJobDataMap().getString("socketId");
        System.out.println(LocalDateTime.now() + ": job 1 doing something...");
        System.out.println("定时任务执行中…");
    }
}