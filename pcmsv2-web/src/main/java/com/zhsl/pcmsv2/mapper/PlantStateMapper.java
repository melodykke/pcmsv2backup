package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.PlantState;

import java.util.List;

public interface PlantStateMapper {
    int deleteByPrimaryKey(Integer stateId);

    int insert(PlantState record);

    PlantState selectByPrimaryKey(Integer stateId);

    List<PlantState> selectAll();

    int updateByPrimaryKey(PlantState record);
}