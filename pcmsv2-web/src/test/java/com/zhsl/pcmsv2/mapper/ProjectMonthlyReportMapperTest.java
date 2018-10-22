package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.BaseInfo;
import com.zhsl.pcmsv2.model.ProjectMonthlyReport;
import com.zhsl.pcmsv2.service.MonthReportService;
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
public class ProjectMonthlyReportMapperTest {

    @Autowired
    private ProjectMonthlyReportMapper projectMonthlyReportMapper;

    @Test
    public void findByBaseInfoIdsInWithImg() throws Exception {

        List<String> baseInfoIds = new ArrayList<>();

        baseInfoIds.add("8a8082816458ab31016458ab49d40091");
        baseInfoIds.add("4028e4ec64acd3340164acd6159b0007");

        List<ProjectMonthlyReport> projectMonthlyReports = projectMonthlyReportMapper.findByBaseInfoIdsInWithImg(baseInfoIds);
        System.out.println(projectMonthlyReports);
    }

}