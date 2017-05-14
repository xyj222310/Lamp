package com.yjtse.dao;

import com.yjtse.entity.Socket;

/**
 * Created by yjtse on 2017/4/5.
 */
public interface SocketDao {
    /**
     * 根据id查询用户各类信息
     *
     * @param socketId
     * @return
     */
    Socket findById(String socketId);

    int addSocket(Socket socket);

    int updateSocket(Socket socket);

//    /*
//    修改定时参数
//     */
//    int updateCron();

    int deleteById(String socketId);
}
