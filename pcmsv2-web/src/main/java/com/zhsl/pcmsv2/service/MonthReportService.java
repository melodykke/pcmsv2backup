package com.zhsl.pcmsv2.service;


import com.zhsl.pcmsv2.dto.ProjectMonthlyReportDTO;
import com.zhsl.pcmsv2.model.ProjectMonthlyReport;
import com.zhsl.pcmsv2.vo.ProjectMonthlyReportVO;

import java.math.BigDecimal;
import java.util.List;

public interface MonthReportService {

    /**
     * 创建月报
     * @param projectMonthlyReportDTO
     * @return
     */
    int create(ProjectMonthlyReportDTO projectMonthlyReportDTO);

    /**
     * 通过ID删除月报 自己删除自己的记录
     * @param pmrId
     * @return
     */
    int delete(String pmrId);


    /**
     * 通过ID删除月报 管理员删除记录的方法
     * @param pmrId
     * @return
     */
    int managementDelete(String pmrId);

    /**
     * 修改月报  管理员修改记录的方法
     * @param projectMonthlyReportDTO
     * @return
     */
    int managementModify(ProjectMonthlyReportDTO projectMonthlyReportDTO);

    /**
     * 通过ID查找月报
     * @param pmrId
     * @return
     */
    ProjectMonthlyReportVO findById(String pmrId);

    /**
     * 通过ID查找月报 管理员查找月报的方法
     * @param pmrId
     * @return
     */
    ProjectMonthlyReportVO managementFindById(String pmrId);

    /**
     * 查找全部月报
     * @return
     */
    List<ProjectMonthlyReportVO> findAll();

    /**
     * 通过baseInfoID和时间段查找该时间段内月报和图片
     * @param baseInfoId
     * @param startDate 2018-08-01
     * @param endDate
     * @return
     */
    List<ProjectMonthlyReportVO> findByBaseInfoIdAndPeriodWithImg(String startDate, String endDate);

    /**
     * 查找属于某一个水库的所有月报和图片
     * @return
     */
    List<ProjectMonthlyReportVO> findByBaseInfoIdWithImg();

    /**
     * 根据登陆用户所属地区获取该区域下的所有月报信息
     * @return
     */
    List<ProjectMonthlyReport> findAllPmrViaUserRegion();

    /**
     * 计算所有工程的投资完成情况
     * @return
     */
    BigDecimal calcOverallInvestmentCompletion();

    /**
     * 和Redis缓存同步
     */
    void syncToRedis();
}
