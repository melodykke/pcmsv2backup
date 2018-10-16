package com.zhsl.pcmsv2.controller;

import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id:\\d+}")
    public UserInfo getInfo(@PathVariable String id) {
        System.out.println("进入getuserinfo服务");
        UserInfo userInfo = userService.findById(id);
        return userInfo;
    }
}
