package com.yjtse.web;

import com.yjtse.dto.Result;
import com.yjtse.entity.User;
import com.yjtse.service.UserService;
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
@RequestMapping("/user") // url:/模块/资源/{id}/细分 /seckill/list
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 通过账号查询用户信息
     * ajax json
     */
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    private Result<User> getById(@PathVariable("userId") String userId, Model model) {

        if (userId != null) {
            User user = userService.getById(userId);
            if (user != null) {
                return new Result<>(true, user);
            }
            return new Result<>(false, "User non exist");
        }
        return new Result<>(false, "wrong ID");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result<User> updateById(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "userPass") String userPass,
            @RequestParam(value = "mail", required = false) String mail,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "sex", required = false) String sex,
            @RequestParam(value = "icon", required = false) String icon,
            @RequestParam(value = "role", required = false) String role) {

        if (userId != null && userPass != null) {
            User user = new User(userId, mail, phone, userName, userPass, sex, icon, role);
            if (userService.updateUser(user) == 1) {
                return new Result<>(true, "Update Success!");
            }
            return new Result<>(false, "Update Failed!");
        }
        return new Result<>(false, "Error Input!");
    }

    /**
     * 注册用户
     *
     * @param userId
     * @param userPass
     * @param mail
     * @param phone
     * @param userName
     * @param sex
     * @param icon
     * @param role
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result<User> register(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "userPass") String userPass,
            @RequestParam(value = "mail", required = false) String mail,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "sex", required = false) String sex,
            @RequestParam(value = "icon", required = false) String icon,
            @RequestParam(value = "role", required = false) String role) {

        if (userId != null && userPass != null) {
            if (userService.addUser(new User(userId, mail, phone, userName, userPass, sex, icon, role)) == 1) {
                User user = userService.getById(userId);
                return new Result<>(true, "Registered!");
            }
            return new Result<>(false, "Register Failed,please check your Input");
        }
        return new Result<>(false, "Please Input Account & Password");

    }

}