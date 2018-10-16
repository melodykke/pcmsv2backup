package com.zhsl.pcmsv2.service.impl;

import com.zhsl.pcmsv2.mapper.UserInfoMapper;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("用户名为：" + username + "的用户正在尝试登陆...");
        // 根据用户名查找用户信息 TODO
        UserInfo userInfo = userInfoMapper.findByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("不存在此用户！");
        }
        return userInfo;
    }

    @Override
    public UserInfo findById(String id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        return userInfo;
    }
}
