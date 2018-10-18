package com.zhsl.pcmsv2.controller.management;

import com.zhsl.pcmsv2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理接口类 包含对账户的管理
 */
@RestController
@RequestMapping("/management")
public class ManagementController {

    @Autowired
    private UserService userService;

    
}
