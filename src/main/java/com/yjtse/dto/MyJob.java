package com.yjtse.dto;//package com.yjtse.service;

import com.yjtse.dao.SocketDao;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//public class MyJob implements Job {
@Service
public class MyJob implements Job {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocketDao socketDao;

    private int timeout;
    private String socketId;
    private static int i = 0;

    //调度工厂实例化后，经过timeout时间开始执行调度
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    /**
     * 要调度的具体任务
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //获取要修改的状态的插座的id
        jobExecutionContext.getJobDetail().getKey();
//        String socketId = jobExecutionContext.getMergedJobDataMap().getString("socketId");
//        if (socketId != null) {
//            Socket socket = socketDao.findById(socketId);
//            if (socket != null) {
//                socket.setStatus(socket.getStatusTobe());//按照定时要求的状态将插座设置为该状态
////                socketDao.updateSocket(socket);
//            }
        JobDataMap map = jobExecutionContext.getJobDetail().getJobDataMap();
        if (map != null) {
            map.getString("socketId");
            map.getString("statusTobe");
        }
        System.out.println(LocalDateTime.now() + ": job 1 doing something...");
        System.out.println("\n" + map.getString("socketId"));
//        }

    }
}