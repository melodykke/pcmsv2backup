package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.ProjectMonthlyReportImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMonthlyReportImgMapper {
    /**
     * 根据主键删除记录
     * @param projectMonthlyReportImgId
     * @return
     */
    int deleteByPrimaryKey(String projectMonthlyReportImgId);

    /**
     * 新增记录
     * @param record
     * @return
     */
    int insert(ProjectMonthlyReportImg record);

    /**
     * 根据主键查询记录
     * @param projectMonthlyReportImgId
     * @return
     */
    ProjectMonthlyReportImg selectByPrimaryKey(String projectMonthlyReportImgId);

    /**
     * 查找全部记录
     * @return
     */
    List<ProjectMonthlyReportImg> selectAll();

    /**
     * 更新记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(ProjectMonthlyReportImg record);

    /**
     * 批量添加记录
     * @param projectMonthlyReportImgs
     * @return
     */
    int batchInsert(List<ProjectMonthlyReportImg> projectMonthlyReportImgs);

    /**
     * 根据月报id删除月报图片
     * @param projectMonthlyReportId
     * @return
     */
    int deleteByPmrId(String projectMonthlyReportId);
}