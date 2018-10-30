package com.zhsl.pcmsv2.service;

import com.zhsl.pcmsv2.vo.RegionInvestmentVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticServiceTest {

    @Autowired
    private StatisticService statisticService;

    @Test
    public void calcRegionInvestmentStatistic() throws Exception {

       // RegionInvestmentVO regionInvestmentVO = statisticService.calcRegionInvestmentStatistic(7, "2018-01-01", "2018-08-01");

        //System.out.println(regionInvestmentVO);
    }

}