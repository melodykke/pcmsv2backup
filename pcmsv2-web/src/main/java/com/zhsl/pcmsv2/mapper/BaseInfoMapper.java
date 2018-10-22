package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.BaseInfo;
import com.zhsl.pcmsv2.model.Region;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseInfoMapper {
    /**
     * 根据主键删除水库基础信息
     * @param baseInfoId
     * @return
     */
    int deleteByPrimaryKey(String baseInfoId);

    /**
     * 增加水库基本信息
     * @param record
     * @return
     */
    int insert(BaseInfo record);

    /**
     * 根据主键查找对应水库基础信息
     * @param baseInfoId
     * @return
     */
    BaseInfo selectByPrimaryKey(String baseInfoId);

    /**
     * 查找所有水库基本信息
     * @return
     */
    List<BaseInfo> selectAll();

    /**
     * 更新指定水库信息
     * @param record
     * @return
     */
    int updateByPrimaryKey(BaseInfo record);

    /**
     * 查找所属区域的水库群
     * @param regionId
     * @return
     */
    List<BaseInfo> findByRegion(Region region);

    /**
     * 查找所有水库名 （防止命名重复）
     * @return
     */
    List<String> findAllPlantName();

    /**
     *
     */
    int batchInsert(@Param("baseInfos") List<BaseInfo> baseInfos);

    /**
     * 查找属于这些区域的水库
     * @param regions
     * @return
     */
    List<BaseInfo> findByRegionsIn(List<Region> regions);
}