package com.yjtse.service;

import com.yjtse.dao.SocketDao;
import com.yjtse.dto.Result;
import com.yjtse.entity.Socket;
import com.yjtse.service.job.MyJob;
import com.yjtse.service.job.QuartzManager;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created by yjtse on 2017/4/5.
 */
@Service
public class SocketService {
    public static String JOB_NAME = "JOB";
    public static String TRIGGER_NAME = "TRIGGER";
    public static String JOB_GROUP_NAME = "TSE_JOB_GROUP";
    public static String TRIGGER_GROUP_NAME = "TSE_TRIGGER_GROUP";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocketDao socketDao;

    private QuartzManager quartzManager = new QuartzManager();

    public Result<Socket> findById(String socketId) {

        if (socketId != null) {
            Socket socket = socketDao.findById(socketId);
            if (socket != null) {
                return new Result<>(true, socket);
            }
            return new Result<>(false, "Socket Doesn't Exist!");
        }
        return new Result<>(false, "Error Input");
    }

    public Result addSocket(Socket socket) {
        if (socket.getSocketId() != null) {
            if (socketDao.addSocket(socket) == 1) {

//                socketService.findById(socketId);
                return new Result<>(true, "Socket Registered!");
            }
//            return new Result<>(false, "Socket Register Failed,please check your Input");
        }
        return new Result<>(false, "Please Input Account & Password");

    }

    public Result updateSocket(Socket socket) {

        if (socket.getSocketId() != null && socket.getOwnerId() != null) {

            if (socketDao.findById(socket.getSocketId()).getOwnerId()
                    .equals(socket.getOwnerId())) {
                //判断用户操作插座权限
                if (socketDao.updateSocket(socket) == 1) {
                    return new Result<>(true, "Update Success!");
                }
                return new Result<>(false, "Operation Failed");
            }
            return new Result<>(false, "Update Failed,Check if UR permitted to do this OP");
        }
        return new Result<>(false, "Error Input!");
    }

    public Result deleteById(String socketId) {
        return (socketDao.deleteById(socketId) == 1) ?
                new Result<>(true, "Deleted!") :
                new Result(false, "Failed!");
    }

    /**
     * 设置插座的定时参数以及定时操作（插座将要变为什么状态?）
     *
     * @param socket
     * @param localDateTime
     * @return
     */
    public Result updateTimerParams(Socket socket, LocalDateTime localDateTime) {

        localDateTime = LocalDateTime.now().plusSeconds(10); //测试一下设置为30s之后
        System.out.println(LocalDateTime.now());
        /**
         * 把时间转为cron参数
         */
        StringBuilder valueBuilder = new StringBuilder(localDateTime.getSecond() + " ");
        valueBuilder.append(localDateTime.getMinute() + " ");
        valueBuilder.append(localDateTime.getHour() + " ");
        valueBuilder.append(localDateTime.getDayOfMonth() + " ");
        valueBuilder.append(localDateTime.getMonth() + " "); //month
        valueBuilder.append("?"); //week
        valueBuilder.append(" " + localDateTime.getYear()); //year
        socket.setCron(valueBuilder.toString());

        /**
         * 更新一下数据库
         */
        socketDao.updateSocket(socket);

        /**
         * 修改定时任务
         */
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring-quartz.xml");
//        QuartzManager quartzManager = (QuartzManager) ctx.getBean("quartzManager");
        try {

            Thread.sleep(500);
            System.out.println("【modify job 】");

            /**
             * 给任务传参数
             */

            Scheduler scheduler = quartzManager.getSchedulerFactory().getScheduler();
            /**
             * 改之前先判断job是否存在
             */
            if (!scheduler.checkExists(JobKey.jobKey(socket.getSocketId() + JOB_NAME, JOB_GROUP_NAME))) {
                quartzManager.addJob(
                        socket.getSocketId() + JOB_NAME,
                        JOB_GROUP_NAME,
                        socket.getSocketId() + TRIGGER_NAME,
                        TRIGGER_GROUP_NAME,
                        MyJob.class,
                        //"0/15 * * * * ?");
                        "0 0 0 1 1 ? 2030",
                        socket,
                        socketDao);
            }

            /**
             * 修改定时的时间
             */
            Thread.sleep(500);
            quartzManager.modifyJobTime(
                    socket.getSocketId() + JOB_NAME,
                    JOB_GROUP_NAME,
                    socket.getSocketId() + TRIGGER_NAME,
                    TRIGGER_GROUP_NAME,
                    valueBuilder.toString(),
                    socket);
//            QuartzManager.startJobs();

            return new Result(true, "创建成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Result(false, "修改定时失败");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new Result(false, "获取任务池失败");
        }
    }
}

