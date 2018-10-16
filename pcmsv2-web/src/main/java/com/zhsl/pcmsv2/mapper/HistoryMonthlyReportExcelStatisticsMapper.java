package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.HistoryMonthlyReportExcelStatistics;

import java.util.List;

public interface HistoryMonthlyReportExcelStatisticsMapper {
    int deleteByPrimaryKey(String hId);

    int insert(HistoryMonthlyReportExcelStatistics record);

    HistoryMonthlyReportExcelStatistics selectByPrimaryKey(String hId);

    List<HistoryMonthlyReportExcelStatistics> selectAll();

    int updateByPrimaryKey(HistoryMonthlyReportExcelStatistics record);

    HistoryMonthlyReportExcelStatistics findByBaseInfoId(String baseInfoId);
}