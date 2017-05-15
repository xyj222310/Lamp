package com.yjtse.web;

import com.yjtse.dto.Result;
import com.yjtse.entity.Socket;
import com.yjtse.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Created by yjtse on 2017/4/5.
 */
@Controller
@RequestMapping("/socket") // url:/模块/资源/{id}/细分 /seckill/list
public class SocketController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocketService socketService;

//    @Autowired
//    private TimerService timerService;


    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result<Socket> registerSocket(
            @RequestParam(value = "socketId") String socketId,
            @RequestParam(value = "socketName", required = false) String socketName,
            @RequestParam(value = "ownerId", required = false) String ownerId) {
        Socket socket = new Socket();
        socket.setSocketId(socketId);
        socket.setSocketName(socketName);
        socket.setOwnerId(ownerId);
        return socketService.addSocket(socket);
    }

    @RequestMapping(value = "/{socketId}", method = RequestMethod.GET)
    @ResponseBody
    private Result<Socket> getById(@PathVariable("socketId") String socketId, Model model) {
        return socketService.findById(socketId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result<Socket> updateById(
            @RequestParam(value = "socketId") String socketId,
            @RequestParam(value = "socketName", required = false) String socketName,
            @RequestParam(value = "ownerId") String ownerId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "cron", required = false) String cron) {
        Socket socket = new Socket();
        socket.setSocketId(socketId);
        socket.setSocketName(socketName);
        socket.setOwnerId(ownerId);
        socket.setStatus(status);
        socket.setCron(cron);
        return socketService.updateSocket(socket);
    }

    @RequestMapping(name = "/delete", method = RequestMethod.DELETE)
    private Result deleteById(
            @RequestParam(value = "socketId") String socketId) {
        return socketService.deleteById(socketId);
    }

    /**
     * 修改定时参数API
     *
     * @param localDateTime
     * @return
     */
    @RequestMapping(value = "/updateCron", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result<Socket> timer(
            @RequestParam(value = "localDateTime", required = true) String localDateTime,
            @RequestParam(value = "statusTobe", required = false) String statusTobe,
            @RequestParam(value = "socketId") String socketId,
            @RequestParam(value = "ownerId") String ownerId) {
        Socket socket = new Socket();
        socket.setSocketId(socketId);
        socket.setOwnerId(ownerId);
        socket.setStatusTobe(statusTobe);
//        LocalDateTime.now();
//        LocalDateTime.parse(localDateTime);
        return socketService.updateTimerParams(socket,
                LocalDateTime.parse(localDateTime));
    }


}