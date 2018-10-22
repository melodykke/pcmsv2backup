package com.zhsl.pcmsv2.service.impl;

import com.zhsl.pcmsv2.mapper.RegionMapper;
import com.zhsl.pcmsv2.model.Region;
import com.zhsl.pcmsv2.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionMapper regionMapper;

    List<Region> children = new ArrayList<>();

    /**
     * 递归查找所有叶子节点（区县）
     * @param regionId
     * @return
     */
    @Override
    public List<Region> findChildrenRecursive(int regionId) {

        Region region = regionMapper.selectByPrimaryKey(regionId);

        List<Region> rootRegionList = new ArrayList<>();

        rootRegionList.add(region);

        recurse(rootRegionList);

        System.out.println(children);

        return children;
    }

    @Override
    public Region findByRegionId(int regionId) {
        return regionMapper.selectByPrimaryKey(regionId);
    }


    public void recurse(List<Region> regions) {

        for (Region region : regions) {
            List<Region> childRegions = regionMapper.findChildrenByParentId(region.getRegionId());
            if (childRegions == null || childRegions.size() == 0) {
                children.add(region);
                continue;
            }
            recurse(childRegions);
        }
    }

}
