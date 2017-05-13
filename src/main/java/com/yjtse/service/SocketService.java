package com.yjtse.service;

import com.yjtse.dao.SocketDao;
import com.yjtse.dto.QuartzManager;
import com.yjtse.dto.Result;
import com.yjtse.entity.Socket;
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
     * 设置插座的定时参数以及定时操作（插座将要变为什么状态?）
     *
     * @param socket
     * @param localDateTime
     * @return
     */
    public Result updateTimerParams(Socket socket, LocalDateTime localDateTime) {
        QuartzManager quartzManager = new QuartzManager();

        localDateTime = LocalDateTime.now().plusSeconds(10); //测试一下设置为10s之后

        StringBuilder valueBuilder = new StringBuilder(localDateTime.getSecond() + " ");
        valueBuilder.append(localDateTime.getMinute() + " ");
        valueBuilder.append(localDateTime.getHour() + " ");
        valueBuilder.append(localDateTime.getDayOfMonth() + " ");
        valueBuilder.append(localDateTime.getMonth() + " "); //month
        valueBuilder.append("?"); //week
        valueBuilder.append(" " + localDateTime.getYear()); //year

        socket.setCron(valueBuilder.toString());
        if (socketDao.updateSocket(socket) == 1) {
            return new Result(true, "Setting Completed!");
        }
        return new Result(false, "Setting Failed!");
    }


}
