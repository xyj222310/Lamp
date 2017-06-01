package com.yjtse.web;

import com.yjtse.dto.Result;
import com.yjtse.entity.Cron;
import com.yjtse.service.CronService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by yjtse on 2017/4/5.
 */
@Controller
@RequestMapping("/cron") // url:/模块/资源/{id}/细分 /seckill/list
public class CronController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CronService cronService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    private Result<Cron> getById(@RequestParam("id") String id, Model model) {
        return cronService.findById(Integer.valueOf(id));
    }

    @RequestMapping(value = "/queryAllByOwnerId", method = RequestMethod.GET)
    @ResponseBody
    private List<Cron> getAllByOwnerId(@RequestParam("ownerId") String ownerId, Model model) {
        return cronService.findAllByOwnerId(ownerId);
    }

    @RequestMapping(value = "/queryBySocketId", method = RequestMethod.GET)
    @ResponseBody
    private List<Cron> getAllBySocketId(@RequestParam("socketId") String socketId, Model model) {
        return cronService.findAllBySocketId(socketId);
    }

    @RequestMapping(value = "/addCron", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result addCron(
            @RequestParam(value = "socketId") String socketId,
            @RequestParam(value = "ownerId") String ownerId,
            @RequestParam(value = "cron") String cron,
            @RequestParam(value = "statusTobe") String statusTobe,
            @RequestParam(value = "available") String available) {
        Cron cron1 = new Cron();
        cron1.setSocketId(socketId);
        cron1.setOwnerId(ownerId);
        cron1.setCron(cron);
        cron1.setStatusTobe(statusTobe);
        cron1.setAvailable(available);
        return cronService.addCron(cron1);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result updateById(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "socketId", required = false) String socketId,
            @RequestParam(value = "ownerId", required = false) String ownerId,
            @RequestParam(value = "statusTobe") String statusTobe,
            @RequestParam(value = "cron") String cron,
            @RequestParam(value = "available") String available) {
        Cron cron1 = new Cron();
        cron1.setId(Integer.valueOf(id));
        cron1.setSocketId(socketId);
        cron1.setOwnerId(ownerId);
        cron1.setStatusTobe(statusTobe);
        cron1.setCron(cron);
        cron1.setAvailable(available);
        return cronService.updateCron(cron1);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result deleteById(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "ownerId") String ownerId) {
        return cronService.deleteById(Integer.valueOf(id), ownerId);
    }

    @RequestMapping(value = "/deleteByOwnerId", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result deleteByOwnerId(
            @RequestParam(value = "ownerId") String ownerId) {
        return cronService.deleteByOwnerId(ownerId);
    }

    @RequestMapping(value = "/deleteBySocketId", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result deleteBySocketId(
            @RequestParam(value = "socketId") String socketId) {
        return cronService.deleteBySocketId(socketId);
    }
}