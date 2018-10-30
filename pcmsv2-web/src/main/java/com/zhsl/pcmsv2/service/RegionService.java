package com.zhsl.pcmsv2.service;

import com.zhsl.pcmsv2.model.Region;

import java.util.List;

public interface RegionService {

    /**
     * 递归查找指定ID的子项 直到最低层 查找叶子
     * @param regionId
     * @return
     */
    List<Region> findLeafRecursive(int regionId);

    /**
     * 主键查找区域
     * @param regionId
     * @return
     */
    Region findByRegionId(int regionId);

    /**
     * 只有最高级用户才能调用，返回排除省的所有区域
     * @return
     */
    List<Region> findAll();

    /**
     * 查找包含自身的所有节点（包括节点和叶子节点还有root）
     * @param regionId
     * @return
     */
    List<Region> findNodeRecursive(Integer regionId);

    /**
     * 获取下一级区域
     * @param regionId
     * @return
     */
    List<Region> findSubRegions(int regionId);
}
