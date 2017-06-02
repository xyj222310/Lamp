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
import java.util.List;

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

    public List<Socket> findAllByUserId(String userId) {
        return socketDao.findAllByUserId(userId);
    }

    public Result<Socket> addSocket(Socket socket) {
        if (socket.getSocketId() != null) {
            if (socketDao.addSocket(socket) == 1) {
                return new Result<>(true, socketDao.findById(socket.getSocketId()));
            }
            return new Result<>(false, "Socket already exist");
        }
        return new Result<>(false, "input error ");

    }

    public Result updateSocket(Socket socket) {

        if (socket.getSocketId() != null && socket.getOwnerId() != null) {

            //判断用户操作插座权限
            if (socketDao.findById(socket.getSocketId()).getOwnerId()
                    .equals(socket.getOwnerId())) {
                if (socketDao.updateSocket(socket) == 1) {
                    return new Result<>(true, "Update Success!");
                }
                return new Result<>(false, "Operation Failed");
            }
            return new Result<>(false, "Update Failed,Check if UR permitted to do this OP");
        }
        return new Result<>(false, "Error Input!");
    }

    public Result deleteById(String socketId, String ownerId) {
        if (socketDao.findById(socketId).getOwnerId().equals(ownerId)) {
            return (socketDao.deleteById(socketId) == 1) ?
                    new Result<>(true, "Deleted!") :
                    new Result(false, "Failed!");
        }
        return (new Result<>(false, "not permmited to this"));
    }

    /**
     * 设置插座的定时参数以及定时操作（插座将要变为什么状态?）
     * 废弃了
     *
     * @param socket
     * @param localDateTime
     * @return
     */
    public Result updateTimerParams(Socket socket, LocalDateTime localDateTime) {
        /**
         * 把时间转为cron参数
         */
        StringBuilder valueBuilder = new StringBuilder(localDateTime.getSecond() + " ");
        valueBuilder.append(localDateTime.getMinute() + " ");
        valueBuilder.append(localDateTime.getHour() + " ");
        valueBuilder.append(localDateTime.getDayOfMonth() + " ");
        valueBuilder.append(localDateTime.getMonthValue() + " "); //month
        valueBuilder.append("?"); //week
        valueBuilder.append(" " + localDateTime.getYear()); //year
        socket.setCron(valueBuilder.toString());

        /**
         * 更新一下数据库
         */
        //判断用户操作插座权限
        if (socketDao.findById(socket.getSocketId()).getOwnerId()
                .equals(socket.getOwnerId())) {
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
                 * 改之前先判断job是否存在
                 */
                Scheduler scheduler = quartzManager.getSchedulerFactory().getScheduler();

                if (!scheduler.checkExists(JobKey.jobKey(JOB_NAME + socket.getSocketId(), JOB_GROUP_NAME))) {
                    quartzManager.addJob(
                            JOB_NAME + socket.getSocketId(),
                            JOB_GROUP_NAME,
                            TRIGGER_NAME + socket.getSocketId(),
                            TRIGGER_GROUP_NAME,
                            MyJob.class,
                            //"0/15 * * * * ?");
                            "0 0 0 1 1 ? 2030",
                            socket);
                }
                /**
                 * 修改定时的时间
                 */
                Thread.sleep(500);
                quartzManager.modifyJobTime(
                        JOB_NAME + socket.getSocketId(),
                        JOB_GROUP_NAME,
                        TRIGGER_NAME + socket.getSocketId(),
                        TRIGGER_GROUP_NAME,
                        valueBuilder.toString(),
                        socket);
//            QuartzManager.startJobs();
                return new Result(true, "success");
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new Result(false, "failed");
            } catch (SchedulerException e) {
                e.printStackTrace();
                return new Result(false, "error to get JOB pool");
            }

        } else return new Result(false, "check your permission!");
    }
}

