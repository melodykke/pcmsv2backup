package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.ProjectStatus;

import java.util.List;

public interface ProjectStatusMapper {
    int deleteByPrimaryKey(String projectStatusId);

    int insert(ProjectStatus record);

    ProjectStatus selectByPrimaryKey(String projectStatusId);

    List<ProjectStatus> selectAll();

    int updateByPrimaryKey(ProjectStatus record);

    List<ProjectStatus> findByBaseInfoId(String baseInfoId);
}