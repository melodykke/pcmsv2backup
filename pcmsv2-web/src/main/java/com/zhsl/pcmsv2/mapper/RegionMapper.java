package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.Region;

import java.util.List;

public interface RegionMapper {
    int deleteByPrimaryKey(Integer regionId);

    int insert(Region record);

    Region selectByPrimaryKey(Integer regionId);

    List<Region> selectAll();

    int updateByPrimaryKey(Region record);

    List<Region> findByParentIdNotIn(List<Integer> list);

    List<Region> findByParentId(Integer parentId);
}