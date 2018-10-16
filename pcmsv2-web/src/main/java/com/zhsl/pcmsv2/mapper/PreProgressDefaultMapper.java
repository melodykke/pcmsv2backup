package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.PreProgressDefault;

import java.util.List;

public interface PreProgressDefaultMapper {
    int deleteByPrimaryKey(Integer preProgressDefaulId);

    int insert(PreProgressDefault record);

    PreProgressDefault selectByPrimaryKey(Integer preProgressDefaulId);

    List<PreProgressDefault> selectAll();

    int updateByPrimaryKey(PreProgressDefault record);
}