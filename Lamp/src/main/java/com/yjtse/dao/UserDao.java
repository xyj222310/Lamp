package com.yjtse.dao;

import com.yjtse.entity.User;

/**
 * Created by yjtse on 2017/4/5.
 */
public interface UserDao {
    /**
     * 根据id查询用户各类信息
     *
     * @param userId
     * @return
     */
    User findById(String userId);

    int addUser(User user);

    int updateUser(User user);
}
