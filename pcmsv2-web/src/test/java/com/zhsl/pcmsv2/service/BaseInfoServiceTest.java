package com.zhsl.pcmsv2.service;

import com.zhsl.pcmsv2.mapper.BaseInfoMapper;
import com.zhsl.pcmsv2.model.BaseInfo;
import com.zhsl.pcmsv2.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseInfoServiceTest {

    @Autowired
    private BaseInfoMapper baseInfoMapper;

    @Test
    public void changeId2UUID() {

     /*   List<BaseInfo> baseInfoList = baseInfoMapper.selectAll();

        List<BaseInfo> newBaseInfoList = new ArrayList<>();

        newBaseInfoList = baseInfoList.stream().map(e -> change(e)).collect(Collectors.toList());

        baseInfoMapper.batchInsert(newBaseInfoList);

        System.out.println("批量添加完毕");*/

    /*    List<BaseInfo> baseInfoList = baseInfoMapper.selectAll();

        List<BaseInfo> newBaseInfoList = new ArrayList<>();


        newBaseInfoList.add(baseInfoList.get(13));

        newBaseInfoList = newBaseInfoList.stream().map(e -> change(e)).collect(Collectors.toList());

        System.out.println(newBaseInfoList);

        baseInfoMapper.batchInsert(newBaseInfoList);

        System.out.println("批量添加完毕");*/

    }

/*    public BaseInfo change(BaseInfo baseInfo) {

        String originalId = baseInfo.getBaseInfoId();

        StringBuilder stringBuilder = new StringBuilder(originalId);

        stringBuilder.insert(8, "-");
        stringBuilder.insert(13, "-");
        stringBuilder.insert(18, "-");
        stringBuilder.insert(23, "-");

        String newId = stringBuilder.toString().toUpperCase();

        baseInfo.setBaseInfoId(newId);

        return baseInfo;
    }*/
}