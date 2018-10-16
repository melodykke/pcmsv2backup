package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.PersonInfo;

import java.util.List;

public interface PersonInfoMapper {
    int deleteByPrimaryKey(String personId);

    int insert(PersonInfo record);

    PersonInfo selectByPrimaryKey(String personId);

    List<PersonInfo> selectAll();

    int updateByPrimaryKey(PersonInfo record);

    PersonInfo selectByUserId(String userId);
}