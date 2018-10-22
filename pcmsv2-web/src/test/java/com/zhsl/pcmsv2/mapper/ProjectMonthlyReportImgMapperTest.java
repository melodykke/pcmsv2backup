package com.zhsl.pcmsv2.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectMonthlyReportImgMapperTest {

    @Autowired
    private ProjectMonthlyReportImgMapper projectMonthlyReportImgMapper;

    @Test
    public void deleteByPmrId() throws Exception {
        projectMonthlyReportImgMapper.deleteByPmrId("4028e40e6583a47b016583a8bad80006");
    }

}