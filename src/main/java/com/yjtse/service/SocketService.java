package com.yjtse.service;

import com.yjtse.dao.SocketDao;
import com.yjtse.dto.Result;
import com.yjtse.entity.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by yjtse on 2017/4/5.
 */
@Service
public class SocketService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocketDao socketDao;

    public Result findById(String socketId) {

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
//                Socket socket = socketService.findById(socketId);
                return new Result<>(true, "Socket Registered!");
            }
            return new Result<>(false, "Socket Register Failed,please check your Input");
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
     * 设置定时功能
     *
     * @param date
     * @return
     */
    public Result timer(Date date) {
        return new Result(false, "");
    }
}
