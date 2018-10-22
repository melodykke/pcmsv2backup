package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.BaseInfo;
import com.zhsl.pcmsv2.model.Region;
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
public class BaseInfoMapperTest {

    @Autowired
    private BaseInfoMapper baseInfoMapper;

    @Test
    public void findByRegionsIn() throws Exception {

        List<Region> regionIds = new ArrayList<>();

        Region r1 = new Region();
        r1.setRegionId(98);
        Region r2 = new Region();
        r2.setRegionId(68);
        Region r3 = new Region();
        r3.setRegionId(22);

        regionIds.add(r1);
        regionIds.add(r2);
        regionIds.add(r3);

        List<BaseInfo> baseInfoList = baseInfoMapper.findByRegionsIn(regionIds);

        System.out.println(baseInfoList);
    }

}