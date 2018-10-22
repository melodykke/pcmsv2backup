package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.BaseInfo;
import com.zhsl.pcmsv2.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoMapperTest {



    @Autowired
    private UserInfoMapper userInfoMapper;


    @Test
    public void findByUsername() throws Exception {
        UserInfo userInfo = userInfoMapper.findByUsername("zysswj");
        System.out.println(userInfo);
    }

    @Test
    public void findOneWithRolesAndPrivilegesByUsernameOrId() throws Exception {
        UserInfo userInfo = userInfoMapper.findOneWithRolesAndPrivilegesByUsernameOrId("zysswj", null);
        System.out.println(userInfo);
    }

    @Test
    public void findAllUsername() throws Exception {
        List<String> usernames = userInfoMapper.findAllUsername();
        System.out.println(usernames);
    }

    @Test
    public void selectByPrimaryKey() throws Exception {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey("56B122AD-AABF-43G4-B14F-DBBREF65LYD2");
        System.out.println(userInfo);
    }

    @Test
    public void selectAll() throws Exception {
        List<UserInfo> userInfos = userInfoMapper.selectAll();
        System.out.println(userInfos);
    }

    @Test
    public void findOneWithRolesByUsernameOrId() throws Exception {
        UserInfo userInfo =  userInfoMapper.findOneWithRolesByUsernameOrId(null, "56B122AD-AABF-43G4-B14F-DBBREF65LYD2");
        System.out.println(userInfo);
    }

    @Test
    public void findParentByParentId() throws Exception {
        UserInfo userInfo1 =  userInfoMapper.findOneWithRolesByUsernameOrId(null, "56B122AD-AABF-43G4-B14F-DBBREF65LYD2");
        UserInfo userInfo2 =  userInfoMapper.findParentByParentId(userInfo1.getParentId());
        System.out.println(userInfo2);
    }

    @Test
    public void findChildrenByUserId() throws Exception {
        List<UserInfo> userInfos = userInfoMapper.findChildrenByUserId("GCGLB567-0000-0000-0000-000000000000");
        System.out.println(userInfos);
    }

    @Test
    public void findAllInferior() throws Exception {
        List<UserInfo> userInfos = userInfoMapper.findAllInferior("GCGLB567-0000-0000-0000-000000000000");
        System.out.println(userInfos);
    }
   /* @Test
    public void changeBaseInfoIdToUUID() throws Exception {

        List<UserInfo> userInfos = userInfoMapper.selectAll();

        for (UserInfo userInfo : userInfos) {
            if (userInfo.getBaseInfoId() == null) {
                continue;
            }
            UserInfo userInfo1 = change(userInfo);
            userInfoMapper.changeBaseInfoIdToUUID(userInfo1.getBaseInfoId(), userInfo1.getUserId());
        }

        System.out.println("change complete");

    }

    public UserInfo change(UserInfo userInfo) {

        String originalId = userInfo.getBaseInfoId();

        StringBuilder stringBuilder = new StringBuilder(originalId);

        stringBuilder.insert(8, "-");
        stringBuilder.insert(13, "-");
        stringBuilder.insert(18, "-");
        stringBuilder.insert(23, "-");

        String newId = stringBuilder.toString().toUpperCase();

        userInfo.setBaseInfoId(newId);

        return userInfo;
    }*/
}