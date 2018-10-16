package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.ReservoirCode;

import java.util.List;

public interface ReservoirCodeMapper {
    int deleteByPrimaryKey(Integer reservoirId);

    int insert(ReservoirCode record);

    ReservoirCode selectByPrimaryKey(Integer reservoirId);

    List<ReservoirCode> selectAll();

    int updateByPrimaryKey(ReservoirCode record);

    ReservoirCode findByReservoirName(String reservoirName);

    ReservoirCode findByBaseInfoId(String baseInfoId);
}