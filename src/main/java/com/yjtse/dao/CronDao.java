package com.yjtse.dao;

import com.yjtse.entity.Cron;

import java.util.List;

/**
 * Created by yjtse on 2017/4/5.
 */
public interface CronDao {

    Cron findById(Integer id);

    /**
     * 根据插座id查询所有该插座的定时内容
     *
     * @param socketId
     * @return
     */
    List<Cron> findAllBySocketId(String socketId);

    List<Cron> findAllByOwnerId(String ownerId);

    int addCron(Cron cron);

    int updateCron(Cron cron);

    int deleteById(Integer id);

    int deleteBySocketId(String socketId);

    int deleteByOwnerId(String ownerId);
}
