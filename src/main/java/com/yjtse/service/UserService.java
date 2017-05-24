package com.yjtse.service;

import com.yjtse.dao.UserDao;
import com.yjtse.dto.Result;
import com.yjtse.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yjtse on 2017/4/5.
 */
@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 注入Service依赖
    @Autowired
    private UserDao userDao;

    public Result getById(String userId) {

        if (userId != null) {
            User user = userDao.findById(userId);
            if (user != null) {
                return new Result<>(true, user);
            }
            return new Result<>(false, "User non exist");
        }
        return new Result<>(false, "wrong ID");
    }

    public Result login(String userId, String userPass) {

        if (userId != null) {
            User user = userDao.findById(userId);
            if (userPass != null && userPass.equals(user.getUserPass())) {
                return new Result<>(true, user);
            }
            return new Result<>(false, "wrong password");
        }
        return new Result<>(false, "wrong ID");
    }

    public Result addUser(User user) {

        if (user.getUserId() != null && user.getUserPass() != null) {
            if (userDao.addUser(user) == 1) {
//                User user = userDao.findById(user.getUserId());
                return new Result<>(true, "Registered!");
            }
            return new Result<>(false, "Register Failed,please check your Input");
        }
        return new Result<>(false, "Please Input Account & Password");

    }

    public Result updateUser(User user) {

        if (user.getUserId() != null && user.getUserPass() != null) {
            if (userDao.updateUser(user) == 1) {
                return new Result<>(true, "Update Success!");
            }
            return new Result<>(false, "Update Failed!");
        }
        return new Result<>(false, "Error Input!");
    }

    public Result<User> updateUserPass(String userId, String userPass) {
        if (userId != null && userPass != null) {
            User user = new User();
            user.setUserId(userId);
            user.setUserPass(userPass);
            if (userDao.updateUser(user) == 1) {
                return new Result<>(true, "Update Success!");
            }
            return new Result<>(false, "Update Failed!");
        }
        return new Result<>(false, "Error Input!");
    }
}
