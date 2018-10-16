package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.PreProgressImg;

import java.util.List;

public interface PreProgressImgMapper {
    int deleteByPrimaryKey(String preProgressImgId);

    int insert(PreProgressImg record);

    PreProgressImg selectByPrimaryKey(String preProgressImgId);

    List<PreProgressImg> selectAll();

    int updateByPrimaryKey(PreProgressImg record);
}