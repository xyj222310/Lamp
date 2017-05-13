//package com.yjtse.dto;
//
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//import java.util.logging.Logger;
//
///**
// * Created by yjtse on 2017/4/5.
// */
//
///**
// * 定时任务运行（反射出来的类）
// *
// * @author qgw
// *         2016 下午2:39:37 ^_^
// * @Description
// */
//@DisallowConcurrentExecution
//public class QuartzJobFactory implements Job {
//
//    private static final Logger log = Logger.getLogger("");
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//
//        log.info("任务运行开始-------- start --------");
//        try {
//            //ScheduleJob任务运行时具体参数，可自定义
//            ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap()
//                    .get("scheduleJob");
//        } catch (Exception e) {
//            log.info("捕获异常===" + e);
//        }
//        log.info("任务运行结束-------- end --------");
//    }
//}