package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.Region;

import java.util.List;

public interface RegionMapper {
    /**
     * 根据主键删除记录
     * @param regionId
     * @return
     */
    int deleteByPrimaryKey(Integer regionId);

    /**
     * 添加新记录
     * @param record
     * @return
     */
    int insert(Region record);

    /**
     * 根据主键查找
     * @param regionId
     * @return
     */
    Region selectByPrimaryKey(Integer regionId);

    /**
     * 查找全部
     * @return
     */
    List<Region> selectAll();

    /**
     * 更新记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(Region record);

    /**
     * 找出 除开父ID集的 的记录
     * @param list
     * @return
     */
    List<Region> findByParentIdNotIn(List<Integer> list);

    /**
     * 根据 父ID查找
     * @param parentId
     * @return
     */
    List<Region> findChildrenByParentId(Integer parentId);


}