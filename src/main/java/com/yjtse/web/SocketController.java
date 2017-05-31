package com.yjtse.web;

import com.yjtse.dto.Result;
import com.yjtse.entity.Socket;
import com.yjtse.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by yjtse on 2017/4/5.
 */
@Controller
@RequestMapping("/socket") // url:/模块/资源/{id}/细分 /seckill/list
public class SocketController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocketService socketService;

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result<Socket> registerSocket(
            @RequestParam(value = "socketId") String socketId,
            @RequestParam(value = "socketName", required = false) String socketName,
            @RequestParam(value = "ownerId") String ownerId,
            @RequestParam(value = "status", required = false) String status) {
        Socket socket = new Socket();
        socket.setSocketId(socketId);
        socket.setSocketName(socketName);
        socket.setOwnerId(ownerId);
        socket.setStatus(status);
        return socketService.addSocket(socket);
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    private Result<Socket> getById(@RequestParam("socketId") String socketId, Model model) {
        return socketService.findById(socketId);
    }

    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    @ResponseBody
    private List<Socket> getAllByUserId(@RequestParam("ownerId") String ownerId, Model model) {
        return socketService.findAllByUserId(ownerId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result updateById(
            @RequestParam(value = "socketId") String socketId,
            @RequestParam(value = "socketName", required = false) String socketName,
            @RequestParam(value = "ownerId") String ownerId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "available", required = false) String available,
            @RequestParam(value = "cron", required = false) String cron) {
        Socket socket = new Socket();
        socket.setSocketId(socketId);
        socket.setSocketName(socketName);
        socket.setOwnerId(ownerId);
        socket.setStatus(status);
        socket.setCron(cron);
        socket.setAvailable(available);
        return socketService.updateSocket(socket);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result deleteById(
            @RequestParam(value = "socketId") String socketId,
            @RequestParam(value = "ownerId") String ownerId) {
        return socketService.deleteById(socketId, ownerId);
    }

    /**
     * 修改定时参数API
     *
     * @param localDateTime
     * @return
     */
//    @RequestMapping(value = "/updateCron", method = RequestMethod.POST, produces = {
//            "application/json; charset=utf-8"})
//    @ResponseBody
    private Result timer(
            @RequestParam(value = "localDateTime") String localDateTime,
            @RequestParam(value = "statusTobe") String statusTobe,
            @RequestParam(value = "socketId") String socketId,
            @RequestParam(value = "ownerId") String ownerId) {
        Socket socket = new Socket();
        socket.setSocketId(socketId);
        socket.setOwnerId(ownerId);
        socket.setStatusTobe(statusTobe);
//        LocalDateTime.now();
//        LocalDateTime.parse(localDateTime);
        try {
            return socketService.updateTimerParams(socket,
                    LocalDateTime.parse(localDateTime));
        } catch (Exception e) {
            return new Result<Socket>(false, "data format error");
        }
    }
}