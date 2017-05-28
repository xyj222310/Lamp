package com.yjtse.dao;

import com.yjtse.entity.User;
import org.apache.ibatis.annotations.Param;

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
    User findById(@Param("userId") String userId,
                  @Param("phone") String phone,
                  @Param("mail") String mail);

    int addUser(User user);

    int updateUser(User user);
}
