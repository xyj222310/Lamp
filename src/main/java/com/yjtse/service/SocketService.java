package com.yjtse.service;

import com.yjtse.dao.SocketDao;
import com.yjtse.dto.Result;
import com.yjtse.entity.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yjtse on 2017/4/5.
 */
@Service
public class SocketService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocketDao socketDao;

    public Socket findById(String socketId) {
        return socketDao.findById(socketId);
    }

    public Result addSocket(Socket socket) {
        int i = socketDao.addSocket(socket);
        if (socket.getSocketId() != null) {
            if (socketDao.addSocket(socket) == 1) {
//                Socket socket = socketService.findById(socketId);
                return new Result<>(true, "Socket Registered!");
            }
            return new Result<>(false, "Socket Register Failed,please check your Input");
        }
        return new Result<>(false, "Please Input Account & Password");

    }

    public int updateSocket(Socket socket) {
        return socketDao.updateSocket(socket);
    }

    public int deleteById(String socketId) {
        return socketDao.deleteById(socketId);
    }
}
