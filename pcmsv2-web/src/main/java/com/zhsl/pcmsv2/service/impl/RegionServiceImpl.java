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

    /**
     * 递归查找所有叶子节点（区县）
     * @param regionId
     * @return
     */
    @Override
    public List<Region> findChildrenRecursive(int regionId) {

        List<Region> leafRegions = new ArrayList<>();

        Region region = regionMapper.selectByPrimaryKey(regionId);

        List<Region> rootRegionList = new ArrayList<>();

        rootRegionList.add(region);

        List<Region> children = recurse(rootRegionList, leafRegions);

        System.out.println(children);

        return children;
    }

    @Override
    public Region findByRegionId(int regionId) {
        return regionMapper.selectByPrimaryKey(regionId);
    }

    /**
     * 根据root节点查找其所有叶子节点（不包含既是root也时leaf的中间节点）
     * @param rootRegions
     * @param leafRegions
     * @return
     */
    public List<Region> recurse(List<Region> rootRegions, List<Region> leafRegions) {

        for (Region region : rootRegions) {
            List<Region> childRegions = regionMapper.findChildrenByParentId(region.getRegionId());

            if (childRegions != null || childRegions.size() > 0) {
                recurse(childRegions, leafRegions);
            }

            if (childRegions == null || childRegions.size() == 0) {
                leafRegions.add(region);
                continue;
            }

        }

        return leafRegions;
    }

}
