package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.ProjectMonthlyReportImg;

import java.util.List;

public interface ProjectMonthlyReportImgMapper {
    int deleteByPrimaryKey(String projectMonthlyReportImgId);

    int insert(ProjectMonthlyReportImg record);

    ProjectMonthlyReportImg selectByPrimaryKey(String projectMonthlyReportImgId);

    List<ProjectMonthlyReportImg> selectAll();

    int updateByPrimaryKey(ProjectMonthlyReportImg record);

    int batchInsert(List<ProjectMonthlyReportImg> projectMonthlyReportImgs);
}