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
            @RequestParam(value = "ownerId", required = false) String ownerId,
            @RequestParam(value = "status", required = false) String status) {
        return socketService.addSocket(new Socket(socketId, socketName, ownerId, status));
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
            @RequestParam(value = "status", required = false) String status) {

        return socketService.updateSocket(new Socket(socketId, socketName, ownerId, status));
    }

    @RequestMapping(name = "/delete", method = RequestMethod.DELETE)
    private Result deleteById(
            @RequestParam(value = "socketId") String socketId) {
        return socketService.deleteById(socketId);
    }

}