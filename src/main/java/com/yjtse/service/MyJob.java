package com.yjtse.service;

import com.yjtse.dao.SocketDao;
import com.yjtse.dto.Result;
import com.yjtse.entity.Socket;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.time.LocalDateTime;

public class MyJob extends QuartzJobBean {

//    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private SocketDao socketDao;

    @Resource
    private SocketService socketService;
    private int timeout;

    private static int i = 0;

    //调度工厂实例化后，经过timeout时间开始执行调度


    /**
     * 要调度的具体任务
     */
    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
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
            Socket socket = (Socket) map.get("socket");

            if (socket.getSocketId() != null) {
                Result<Socket> socketResult = socketService.findById(socket.getSocketId());
                if (socketResult != null) {
                    Socket socket1 = socketResult.getData();
                    socket1.setStatus(socketResult.getData().getStatusTobe());//按照定时要求的状态将插座设置为该状态
                    socketService.updateSocket(socket1);
                }
            }
            System.out.println(LocalDateTime.now() + ": job 1 doing something...");
            System.out.println(map.getString("socketId"));
        }

    }
}