package com.zhsl.pcmsv2.service;

import com.zhsl.pcmsv2.model.Region;

import java.util.List;

public interface RegionService {

    /**
     * 递归查找指定ID的子项 直到最低层
     * @param regionId
     * @return
     */
    List<Region> findChildrenRecursive(Integer regionId);

}
