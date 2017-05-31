package com.yjtse.service;

import com.yjtse.dao.CronDao;
import com.yjtse.dto.Result;
import com.yjtse.entity.Cron;
import com.yjtse.service.job.MyJob;
import com.yjtse.service.job.QuartzManager;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjtse on 2017/4/5.
 */
@Service
public class CronService {
    public static String JOB_NAME = "JOB";
    public static String TRIGGER_NAME = "TRIGGER";
    public static String JOB_GROUP_NAME = "TSE_JOB_GROUP";
    public static String TRIGGER_GROUP_NAME = "TSE_TRIGGER_GROUP";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CronDao cronDao;

//    private QuartzManager quartzManager = new QuartzManager();

    /**
     * 通过id查找cron列表中一行
     *
     * @param cronId
     * @return
     */
    public Result<Cron> findById(Integer cronId) {
        if (cronId != null) {
            Cron cron = cronDao.findById(cronId);
            if (cron != null) {
                return new Result<>(true, cron);
            }
            return new Result<>(false, "Cron Doesn't Exist!");
        }
        return new Result<>(false, "Error Input");
    }

    /**
     * 查询某个插座设置的所有定时数据
     *
     * @param socketId
     * @return
     */
    public List<Cron> findAllBySocketId(String socketId) {
        try {
            return cronDao.findAllBySocketId(socketId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询某个人设置的所有定时数据
     *
     * @param ownerId
     * @return
     */
    public List<Cron> findAllByOwnerId(String ownerId) {
        try {
            return cronDao.findAllByOwnerId(ownerId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 添加一条定时数据
     *
     * @param cron
     * @return
     */
    public Result addCron(Cron cron) {
        if (!StringUtils.isEmpty(cron.getOwnerId())
                && !StringUtils.isEmpty(cron.getSocketId())) {
            if (cronDao.addCron(cron) == 1) {
                cron.setId(cron.getId());
                updateSchedule(cron);
            }
            return new Result<>(false, "update DB error ");
        } else return new Result(false, "check your permission!");
    }

    public Result updateCron(Cron cron) {
        if (cron.getId() != 0
                && !StringUtils.isEmpty(cron.getOwnerId())
                && !StringUtils.isEmpty(cron.getSocketId())) {
            //判断用户操作插座权限
            try {//更新失败就删了吧
                if (cronDao.findById(cron.getId()).getOwnerId()
                        .equals(cron.getOwnerId())) {
                    //判断用户操作插座权限
                    if (cronDao.updateCron(cron) != 1) {
                        return new Result<>(false, "DB Update failed!");
                    }
                    return updateSchedule(cron);
                }
            } catch (Exception e) {
                cronDao.deleteById(cron.getId());
                return new Result<>(false, "schedule does nt exist,timer will be delete");
            }
            return new Result<>(false, "Operation Failed");
        }
        return new Result<>(false, "Update Failed,Check if UR permitted to do this OP");
    }

    public Result deleteById(Integer id, String ownerId) {
        if (cronDao.findById(id).getOwnerId().equals(ownerId)) {
            List<Cron> list = new ArrayList<>();
            list.add(cronDao.findById(id));
            if (removeSchedule(list).isSuccess()) {
                return (cronDao.deleteById(id) == 1) ?
                        new Result<>(true, "Deleted!") :
                        new Result(false, "Failed!");
            }
            return new Result<>(false, "schedule remove error");
        }
        return new Result<>(false, "not permmited to this");
    }

    public Result deleteByOwnerId(String ownerId) {
        if (removeSchedule(cronDao.findAllBySocketId(ownerId)).isSuccess()) {
            cronDao.deleteBySocketId(ownerId);
            return new Result(true, "succeed");
        }
        return new Result(false, "Failed!");
    }

    public Result deleteBySocketId(String socketId) {
        if (removeSchedule(cronDao.findAllBySocketId(socketId)).isSuccess()) {
            cronDao.deleteBySocketId(socketId);
            return new Result(true, "succeed");
        }
        return new Result(false, "Failed!");


    }

    /**
     * 设置插座的定时参数以及定时操作（插座将要变为什么状态?）
     *
     * @param cron
     * @return
     */
    private Result updateSchedule(Cron cron) {
//        /**
//         * 把时间转为cron参数
//         */
//        StringBuilder valueBuilder = new StringBuilder(localDateTime.getSecond() + " ");
//        valueBuilder.append(localDateTime.getMinute() + " ");
//        valueBuilder.append(localDateTime.getHour() + " ");
//        valueBuilder.append(localDateTime.getDayOfMonth() + " ");
//        valueBuilder.append(localDateTime.getMonthValue() + " "); //month
//        valueBuilder.append("?"); //week
//        valueBuilder.append(" " + localDateTime.getYear()); //year
//        socket.setCron(valueBuilder.toString());

        /**
         * 修改定时任务
         */
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring-quartz.xml");
//        QuartzManager quartzManager = (QuartzManager) ctx.getBean("quartzManager");
        try {
            /**
             * 修改定时的时间
             */
            System.out.println("update job start_______________");
            Thread.sleep(500);
            Scheduler scheduler = QuartzManager.getSchedulerFactory().getScheduler();
            if (!scheduler.checkExists(JobKey.jobKey(JOB_NAME + cron.getId(), JOB_GROUP_NAME))) {
                QuartzManager.addJob(
                        JOB_NAME + cron.getId(),
                        JOB_GROUP_NAME,
                        TRIGGER_NAME + cron.getId(),
                        TRIGGER_GROUP_NAME,
                        MyJob.class,
                        //"0/15 * * * * ?");
                        cron.getCron(),
                        cron);
                System.out.println("add job end_______________");
            } else {
                QuartzManager.modifyJobTime(
                        JOB_NAME + cron.getId(),
                        JOB_GROUP_NAME,
                        TRIGGER_NAME + cron.getId(),
                        TRIGGER_GROUP_NAME,
                        cron.getCron(),
                        cron);
                System.out.println("update job end___________");
            }
            return new Result(true, "success");
//            QuartzManager.startJobs();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Result(false, "Job Interrupted" + e);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new Result(false, "Job schedule Exception" + e);
        }
    }

    private Result removeSchedule(List<Cron> cron) {
        System.out.println("remove job start_______________");
        try {
            for (Cron c : cron) {
                QuartzManager.removeJob(
                        JOB_NAME + c.getId(),
                        JOB_GROUP_NAME,
                        TRIGGER_NAME + c.getId(),
                        TRIGGER_GROUP_NAME);
            }
            return new Result(true, "success");
        } catch (Exception e) {
            return new Result(true, "remove schedule failed");
        }
    }
}

