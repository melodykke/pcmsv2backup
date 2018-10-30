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
    public List<Region> findLeafRecursive(int regionId) {

        List<Region> leafRegions = new ArrayList<>();

        Region region = regionMapper.selectByPrimaryKey(regionId);

        List<Region> rootRegionList = new ArrayList<>();

        rootRegionList.add(region);

        List<Region> children = recurseLeaf(rootRegionList, leafRegions);

        System.out.println(children);

        return children;
    }

    @Override
    public Region findByRegionId(int regionId) {
        return regionMapper.selectByPrimaryKey(regionId);
    }

    /**
     * 只有最高级用户才能调用，返回排除省的所有区域
     * @return
     */
    @Override
    public List<Region> findAll() {
        List<Region> regions = regionMapper.selectAll();
        return regions;
    }

    /**
     * 根据root节点查找其所有叶子节点（不包含既是root也时leaf的中间节点）
     * @param rootRegions
     * @param leafRegions
     * @return
     */
    public List<Region> recurseLeaf(List<Region> rootRegions, List<Region> leafRegions) {

        for (Region region : rootRegions) {
            List<Region> childRegions = regionMapper.findChildrenByParentId(region.getRegionId());

            if (childRegions != null || childRegions.size() > 0) {
                recurseLeaf(childRegions, leafRegions);
            }

            if (childRegions == null || childRegions.size() == 0) {
                leafRegions.add(region);
                continue;
            }

        }

        return leafRegions;
    }

    /**
     * 查找包含自身的所有节点（包括节点和叶子节点还有root）
     * @param regionId
     * @return
     */
    @Override
    public List<Region> findNodeRecursive(Integer regionId) {

        List<Region> nodeRegions = new ArrayList<>();
        List<Region> rootRegions = new ArrayList<>();

        Region rootRegion = regionMapper.selectByPrimaryKey(regionId);

        if (rootRegion == null) {
            log.error("【区域】 查询所有区域node节点时出错，root节点为空！");
            return nodeRegions;
        }

        rootRegions.add(rootRegion);

        nodeRegions = recurseNode(rootRegions, nodeRegions);

        return nodeRegions;
    }

    @Override
    public List<Region> findSubRegions(int regionId) {
        return regionMapper.findChildrenByParentId(regionId);
    }

    /**
     * 递归查找所有node
     * @param rootRegions
     * @param nodeRegions
     * @return
     */
    public List<Region> recurseNode(List<Region> rootRegions, List<Region> nodeRegions) {

        for (Region region : rootRegions) {

            nodeRegions.add(region);

            List<Region> childRegions = regionMapper.findChildrenByParentId(region.getRegionId());


            if (childRegions != null || childRegions.size() > 0) {
                recurseNode(childRegions, nodeRegions);
            }

        }

        return nodeRegions;
    }
}
