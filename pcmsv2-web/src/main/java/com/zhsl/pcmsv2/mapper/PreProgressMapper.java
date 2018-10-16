package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.PreProgress;

import java.util.List;

public interface PreProgressMapper {
    int deleteByPrimaryKey(String preProgressId);

    int insert(PreProgress record);

    PreProgress selectByPrimaryKey(String preProgressId);

    List<PreProgress> selectAll();

    int updateByPrimaryKey(PreProgress record);

    PreProgress findByBaseInfoId(String baseInfoId);

    PreProgress findWithImgByBaseInfoId(String baseInfoId);
}