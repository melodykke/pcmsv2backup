package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.BaseInfo;

import java.util.List;

public interface BaseInfoMapper {
    int deleteByPrimaryKey(String baseInfoId);

    int insert(BaseInfo record);

    BaseInfo selectByPrimaryKey(String baseInfoId);

    List<BaseInfo> selectAll();

    int updateByPrimaryKey(BaseInfo record);

    List<BaseInfo> findByRegionId(int regionId);
}