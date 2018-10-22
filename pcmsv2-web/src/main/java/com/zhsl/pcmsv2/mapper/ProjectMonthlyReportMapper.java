package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.ProjectMonthlyReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMonthlyReportMapper {
    /**
     * 根据主键删除
     * @param projectMonthlyReportId
     * @return
     */
    int deleteByPrimaryKey(String projectMonthlyReportId);

    /**
     * 新增记录
     * @param record
     * @return
     */
    int insert(ProjectMonthlyReport record);

    /**
     * 根据主键查询
     * @param projectMonthlyReportId
     * @return
     */
    ProjectMonthlyReport selectByPrimaryKey(String projectMonthlyReportId);

    /**
     * 查询所有记录
     * @return
     */
    List<ProjectMonthlyReport> selectAll();

    /**
     * 更新记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(ProjectMonthlyReport record);

    /**
     *  根据时间段筛选记录
     * @param baseInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    List<ProjectMonthlyReport> findByBaseInfoIdAndPeriodWithImg(@Param("baseInfoId") String baseInfoId,
                                                                      @Param("startDate") String startDate,
                                                                      @Param("endDate") String endDate);

    /**
     * 根据id查找记录和其图片
     * @param projectMonthlyReportId
     * @return
     */
    ProjectMonthlyReport findByIdWithImg(@Param("projectMonthlyReportId") String projectMonthlyReportId);

    /**
     * 根据baseInfoId查找记录
     * @param baseInfoId
     * @return
     */
    List<ProjectMonthlyReport> findByBaseInfoIdWithImg(@Param("baseInfoId") String baseInfoId);

    /**
     * 根据baseinfoId集合找出对应的月报
     * @param baseInfoIds
     * @return
     */
    List<ProjectMonthlyReport> findByBaseInfoIdsInWithImg(List<String> baseInfoIds);

}