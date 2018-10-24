package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.dto.UsersRoles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserRoleMapperTest {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Test
    public void batchInsert() throws Exception {
        UsersRoles usersRoles = new UsersRoles();
        List<String> userIds = new ArrayList<>();
        List<String> roleIds = new ArrayList<>();
        userIds.add("FFEB9567-6266-4235-9894-CAE5A8D23064");
        userIds.add("FFEB9567-6266-4235-9894-CAE5A8D10642");
        userIds.add("FFEB9567-6266-4235-9894-CAE5A8D23321");
        roleIds.add("0169A85A-8E9D-47AZ-43A5-4B651137AC33");
        roleIds.add("0169A85A-8E9D-47AZ-43A5-4B651137A133");
        usersRoles.setUserIds(userIds);
        usersRoles.setRoleIds(roleIds);
        int result = sysUserRoleMapper.batchInsert(usersRoles);
        System.out.println(result);
    }

    @Test
    public void deleteByUserId() throws Exception {
        int result = sysUserRoleMapper.deleteByUserId("FFEB9567-6266-4235-9894-CAE5A8D23064");
        System.out.println(result);
    }

}