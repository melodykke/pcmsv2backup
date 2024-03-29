package com.zhsl.pcmsv2.service;


import com.zhsl.pcmsv2.dto.ProjectMonthlyReportDTO;
import com.zhsl.pcmsv2.model.ProjectMonthlyReport;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.vo.ProjectMonthlyReportVO;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

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
     * 查询当前用户的在某一时间段内所有的月报记录
     * 如果是管理员 必须传baseInfoId进来查找特定水库的月报
     * @param startDate 2018-08-01
     * @param endDate
     * @return
     */
    List<ProjectMonthlyReportVO> findByBaseInfoIdAndPeriodWithImg(String baseInfoId, String startDate, String endDate);

    /**
     * 查找属于某一个水库的所有月报和图片
     * @return
     */
    List<ProjectMonthlyReportVO> findByBaseInfoIdWithImg();

    /**
     * 根据登陆用户的水库信息获取该其所有月报信息
     * 最高级用户才能向baseInfoId里传值
     * 按时间区间
     * @return
     */
    List<ProjectMonthlyReport> findAllPmrByBaseInfoIdByTimeDuration(String baseInfoId, String startDate, String endDate);

    /**
     * 根据登陆用户所属地区获取该区域下的所有月报信息
     * 只有最高级用户才能向regionId里传非0值
     * 按时间区间
     * @return
     */
    List<ProjectMonthlyReportVO> findAllPmrViaUserRegionByTimeDuration(UserInfo userInfo, int regionId, String startDate, String endDate);

    /**
     * 计算登陆用户所在区域所有工程的投资完成情况
     * 三个参数为选填 startDate 默认从2000年开始 endDate默认为当前时间 regionId默认为0 即查询自身区域内
     * 管理员用户可以传入baseInfoId查询自己辖区内某个水库的情况
     * @return
     */
    BigDecimal calcOverallInvestmentCompletion(String baseInfoId, int regionId, String startDate, String endDate, String by);

    /**
     * 计算登陆用户所在区域所有工程的资金到位情况
     * 三个参数为选填 startDate 默认从2000年开始 endDate默认为当前时间 regionId默认为0 即查询自身区域内
     * 管理员用户可以传入baseInfoId查询自己辖区内某个水库的情况
     * @return
     */
    BigDecimal calcOverallInvestmentAvailable(String baseInfoId, int regionId, String startDate, String endDate, String by);

    /**
     * 和Redis缓存同步
     */
    void syncToRedis();
}
