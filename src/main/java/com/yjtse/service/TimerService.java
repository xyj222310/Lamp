package com.yjtse.service;

import com.yjtse.dto.Result;
import com.yjtse.entity.Socket;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by yjtse on 2017/4/5.
 */
@Service
public class TimerService {

    /**
     * 设置定时功能
     *
     * @param date
     * @param socket
     * @return
     */
    public Result setTimer(Socket socket, Date date) {

        //读取properties配置文件
        Properties prop = new Properties();
        try {
            prop.load(TimerService.class.getResourceAsStream("/set.properties"));
        } catch (IOException e) {
            return new Result(false, "读取配置文件失败：" + e);
            e.printStackTrace();
        }
        String toAccountId = prop.getProperty("toAccountId", "fd60e46db0dc119cfea740c3375fd7c4");
        /*
        1/10 * 8-18 * * ?
        秒    分  时 日 周 月（年，最后一个年份可以隐藏不写）

        从左到右依次是上面的时间维度，这组字符串的含义是：
        每10秒执行一次，分钟不限制，时间控制在8点-18点，日期，周数，月份，年度都不限制
        其中用*表示在此维度上任意一个值都需要执行，用？表示在此维度上不限制，？只能用在日、周、月维度
        第一个维度使用0/10表示每隔10秒执行一次，并且初试时间是0秒，依次执行时间将是0、10、20、30、40、50秒
        如果我们改成3/10，表示初始时间是3秒，依次执行时间将是3、13、23、33、43、53秒
         */
        StringBuilder valueBuilder = new StringBuilder("0/0 ");
        valueBuilder.append(date.getMinutes() + " ");
        valueBuilder.append(date.getHours() + " ");
        valueBuilder.append(date.getDay() + " ");
        valueBuilder.append("?");
        valueBuilder.append("?");
        String cron = prop.getProperty("cron", valueBuilder.toString());

        //创建Quartz的计划任务
        Scheduler sched = null;
        try {
            sched = new StdSchedulerFactory().getScheduler();
        } catch (SchedulerException e) {
            return new Result(false, "创建任务失败：" + e);
            e.printStackTrace();
        }

        //配置计划任务的工作类名，这个类需要实现Job接口，在execute方法中实现所需要做的工作
        JobDetail job = newJob(SetTimer.class).build();
        //向工作类传递参数
        job.getJobDataMap().put("toAccountId", toAccountId);

        //配置计划任务的定时器
        Trigger trigger = newTrigger().withSchedule(cronSchedule(cron)).build();

        Date schDate = null;//获得首次将要执行计划任务的时间，待会儿println出来
        try {
            schDate = sched.scheduleJob(job, trigger);
            sched.start();//开始执行
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new Result(false, "计划任务出错" + e);
        }
        System.out.println("first time run at:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(schDate));

        //无限循环下去
        boolean flag = true;
        try {
            while (flag) {
                Thread.sleep(24 * 3600 * 1000);
            }
            sched.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Result(false, "线程打断 " + e);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new Result(false, " SchedulerException出错" + e);
        }
        return new Result(true, "定时任务执行结束");
    }


}
