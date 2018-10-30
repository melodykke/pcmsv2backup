package com.zhsl.pcmsv2.service;

import com.zhsl.pcmsv2.model.Region;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegionServiceTest {

    @Autowired
    private RegionService regionService;

    @Test
    public void findChildrenRecursive() throws Exception {

        List<Region> leafs = regionService.findLeafRecursive(1);

        for (Region leaf : leafs) {
            System.out.println(leaf.getRegionName());
        }
    }

    @Test
    public void findNodeRecursive() throws Exception {

        List<Region> nodeRegions = regionService.findNodeRecursive(1);

        System.out.println(nodeRegions);

    }
}