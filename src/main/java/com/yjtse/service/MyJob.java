package com.yjtse.service;//package com.yjtse.service;

import com.yjtse.dao.SocketDao;
import com.yjtse.dto.QuartzManager;
import com.yjtse.entity.Socket;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

//public class MyJob implements Job {
public class MyJob {

//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        System.out.println(LocalDateTime.now() + ": job 1 doing something...");
//    }

    @Autowired
    private SocketDao socketDao;

    @Autowired
    private QuartzManager quartzManager;

    public void doSomething(Socket socket) {

        socketDao.updateSocket(socket);
        System.out.println(LocalDateTime.now() + ": job 2 doing something...");
    }
}