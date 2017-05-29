package com.yjtse.web;

import com.yjtse.dto.Result;
import com.yjtse.entity.User;
import com.yjtse.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yjtse on 2017/4/5.
 */
@Controller
@RequestMapping("/user") // url:/模块/资源/{id}/细分 /seckill/list
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 通过账号查询用户信息
     */
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    private Result<User> getById(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "mail", required = false) String mail,
            Model model) {
        return userService.getById(userId, phone, mail);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result<User> login(@RequestParam(value = "userId") String userId,
                               @RequestParam(value = "userPass") String userPass,
                               @RequestParam(value = "phone", required = false) String phone) {
        return userService.login(userId, phone, userPass);

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

        return userService.updateUser(new User(userId, mail, phone, userName, userPass, sex, icon, role));
    }

    @RequestMapping(value = "/updatePass", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    private Result<User> updatePassById(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "userPass") String userPass
    ) {

        return userService.updateUserPass(userId, userPass);
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
        return userService.addUser(new User(userId, mail, phone, userName, userPass, sex, icon, role));

    }

}