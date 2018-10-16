package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoMapperTest {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void findByUsername() throws Exception {
        UserInfo userInfo = userInfoMapper.findByUsername("zatybsk");
        System.out.println(userInfo);
    }

}