package com.yjtse.dto;//package com.yjtse.service;

import com.yjtse.dao.SocketDao;
import com.yjtse.entity.Socket;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

//public class MyJob implements Job {
public class MyJob implements Job {

    @Autowired
    private SocketDao socketDao;

//    @Autowired
//    private QuartzManager quartzManager;

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


//    public void doSomething() {
//
////        socketDao.findById()
////        socketDao.updateSocket(socket);
//        System.out.println(LocalDateTime.now() + ": doSomething doing something...");
//    }

    /**
     * 要调度的具体任务
     */

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //获取要修改的状态的插座的id
        String socketId = jobExecutionContext.getMergedJobDataMap().getString("socketId");

        Socket socket = socketDao.findById(socketId);
        socket.setStatus(socket.getStatusTobe());//按照定时要求的状态将插座设置为该状态
        socketDao.updateSocket(socket);
        System.out.println(LocalDateTime.now() + ": job 1 doing something...");
        System.out.println("\nexecuti...");
    }

}