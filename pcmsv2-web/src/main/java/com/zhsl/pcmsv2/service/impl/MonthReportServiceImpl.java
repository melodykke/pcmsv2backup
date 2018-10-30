package com.zhsl.pcmsv2.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.convertor.dto2model.PmrDTO2Model;
import com.zhsl.pcmsv2.convertor.tovo.Pmr2VO;
import com.zhsl.pcmsv2.dto.ProjectMonthlyReportDTO;
import com.zhsl.pcmsv2.enums.RedisKeys;
import com.zhsl.pcmsv2.enums.RoleEnum;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.mapper.BaseInfoMapper;
import com.zhsl.pcmsv2.mapper.ProjectMonthlyReportImgMapper;
import com.zhsl.pcmsv2.mapper.ProjectMonthlyReportMapper;
import com.zhsl.pcmsv2.model.*;
import com.zhsl.pcmsv2.service.MonthReportService;
import com.zhsl.pcmsv2.service.RegionService;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.util.CalendarUtil;
import com.zhsl.pcmsv2.util.PmrCalculator;
import com.zhsl.pcmsv2.util.RoleCheckUtil;
import com.zhsl.pcmsv2.vo.ProjectMonthlyReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MonthReportServiceImpl implements MonthReportService {

    @Autowired
    private ProjectMonthlyReportMapper pmrMapper;

    @Autowired
    private ProjectMonthlyReportImgMapper pmriMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RegionService regionService;

    @Autowired
    private BaseInfoMapper baseInfoMapper;

    /**
     * 创建新的月报信息
     * @param projectMonthlyReportDTO
     * @return
     */
    @Override
    public int create(ProjectMonthlyReportDTO projectMonthlyReportDTO) {

        if (projectMonthlyReportDTO == null) {
            log.error("【月报】 创建月报时， 收到的月报信息为空");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (thisUser.getBaseInfoId() == null || "".equals(thisUser.getBaseInfoId())) {
            log.error("【月报】 创建月报时， 用户的水库基础信息缺失，无法创建归属某一水库的项目月报");
            throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
        }

        // 检查提交的月报年份和月份是否重复
        List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper.findByBaseInfoIdWithImg(thisUser.getBaseInfoId());

        if (projectMonthlyReports != null && projectMonthlyReports.size() >0) {

            Date submitDate = projectMonthlyReportDTO.getSubmitDate();

            String submittedYearAndMonth = CalendarUtil.getYearAndMonth(submitDate);

            List<String> existYearAndMonths = projectMonthlyReports.stream().map(e -> CalendarUtil.getYearAndMonth(e.getSubmitDate())).collect(Collectors.toList());

            for (String existYearAndMonth : existYearAndMonths) {
                if (submittedYearAndMonth.equals(existYearAndMonth)) {
                    log.error("【月报】 创建月报时， 用户提交的月报时间与其水库已存在月报时间是否重复");
                    throw new SysException(SysEnum.DUPLICATED_RECORD);
                }
            }
        }

        ProjectMonthlyReport projectMonthlyReport = PmrDTO2Model.convert(projectMonthlyReportDTO);

        projectMonthlyReport.setCreateTime(new Date());
        projectMonthlyReport.setUpdateTime(new Date());

        int result = pmrMapper.insert(projectMonthlyReport);

        return result;
    }

    /**
     * 自己删除自己的记录
     * @param pmrId
     * @return
     */
    @Override
    public int delete(String pmrId) {

        if (pmrId == null || "".equals(pmrId)) {
            log.error("【月报】 删除月报时，收到的月报ID为空");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper.findByBaseInfoIdWithImg(thisUser.getBaseInfoId());
        List<String> projectMonthlyReportIds = projectMonthlyReports.stream().map(e -> e.getProjectMonthlyReportId())
                .collect(Collectors.toList());

        if (!projectMonthlyReportIds.contains(pmrId)) {
            log.error("【月报】 删除月报时，不存在要删除的月报信息");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }

        pmriMapper.deleteByPmrId(pmrId);

        int result = pmrMapper.deleteByPrimaryKey(pmrId);

        return result;
    }

    /**
     * 管理员删除月报记录的方法
     * @param pmrId
     * @return
     */
    @Override
    public int managementDelete(String pmrId) {

        if (pmrId == null || "".equals(pmrId)) {
            log.error("【月报】 删除月报时，收到的月报ID为空");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper.selectAll();
        List<String> projectMonthlyReportIds = projectMonthlyReports.stream().map(e -> e.getProjectMonthlyReportId())
                .collect(Collectors.toList());

        if (!projectMonthlyReportIds.contains(pmrId)) {
            log.error("【月报】 删除月报时，不存在要删除的月报信息");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }

        pmriMapper.deleteByPmrId(pmrId);

        int result = pmrMapper.deleteByPrimaryKey(pmrId);

        return result;
    }

    /**
     * 修改更新某条月报记录 (管理员才能更新）
     * @param projectMonthlyReportDTO
     * @return
     */
    @Override
    public int managementModify(ProjectMonthlyReportDTO projectMonthlyReportDTO) {

        if (projectMonthlyReportDTO == null) {
            log.error("【月报】 更新月报时，收到的月报信息为空");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        String pmrId = projectMonthlyReportDTO.getProjectMonthlyReportId();

        if (pmrId == null || "".equals(pmrId)) {
            log.error("【月报】 更新月报时，收到的月报ID为空");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        ProjectMonthlyReport target = pmrMapper.selectByPrimaryKey(pmrId);

        if (target == null) {
            log.error("【月报】 更新月报时，查无要更新的该条月报记录");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }

        String targetId = target.getProjectMonthlyReportId();
        Date submitDate = target.getSubmitDate();
        Date createTime = target.getCreateTime();
        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ProjectMonthlyReport pmr = PmrDTO2Model.convert(projectMonthlyReportDTO);

        BeanUtils.copyProperties(pmr, target);

        target.setProjectMonthlyReportId(targetId);
        target.setSubmitDate(submitDate);
        target.setCreateTime(createTime);
        target.setUpdateTime(new Date());
        target.setSubmitter(thisUser.getName());
        target.setStatisticalLeader(thisUser.getName());

        int result = pmrMapper.insert(target);

        return result;
    }

    /**
     * 通过ID查找月报
     * @param pmrId
     * @return
     */
    @Override
    public ProjectMonthlyReportVO findById(String pmrId) {

        if (pmrId == null || "".equals(pmrId)) {
            log.error("【月报】 查找月报时，收到的月报ID为空");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (thisUser.getBaseInfoId() == null || "".equals(thisUser.getBaseInfoId())) {
            log.error("【月报】 查找月报时， 用户的水库基础信息缺失，无法查找归属某一水库的项目月报");
            throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
        }

        List<ProjectMonthlyReport> pmrs = pmrMapper.findByBaseInfoIdWithImg(thisUser.getBaseInfoId());
        List<String> pmrsStr = pmrs.stream().map(e -> e.getProjectMonthlyReportId()).collect(Collectors.toList());

        if (!pmrsStr.contains(pmrId)) {
            log.error("【月报】 查找月报时，无法查询不属于自己的月报信息");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }

        ProjectMonthlyReport projectMonthlyReport = pmrMapper.findByIdWithImg(pmrId);

        if (projectMonthlyReport == null) {
            log.error("【月报】 查找月报时，查询记录为空");
            return new ProjectMonthlyReportVO();
        }

        ProjectMonthlyReportVO projectMonthlyReportVO = Pmr2VO.convert(projectMonthlyReport);

        return projectMonthlyReportVO;
    }

    /**
     * 根据ID查找月报 管理员用的查询功能
     * @param pmrId
     * @return
     */
    @Override
    public ProjectMonthlyReportVO managementFindById(String pmrId) {

        if (pmrId == null || "".equals(pmrId)) {
            log.error("【月报】 查找月报时，收到的月报ID为空");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Region region = thisUser.getRegion();

        if (region == null) {
            log.error("【月报】 查找月报时，查无用户的区域信息");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }

        if (region.getRegionId() == 0) {
            log.error("【月报】 非法调用，获取用户所在区域所有月报信息。");
            return null;
        }

        List<ProjectMonthlyReport> projectMonthlyReports = findByRegionDuring(null, null, region);
        List<String> projectMonthlyReportIds = projectMonthlyReports.stream().map(e -> e.getProjectMonthlyReportId())
                .collect(Collectors.toList());

        if (!projectMonthlyReportIds.contains(pmrId)) {
            log.error("【月报】 查找月报时，不能查询不属于自己辖区的水库月报");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }

        ProjectMonthlyReport projectMonthlyReport = pmrMapper.findByIdWithImg(pmrId);

        if (projectMonthlyReport == null) {
            log.error("【月报】 查找月报时，查询记录为空");
            return new ProjectMonthlyReportVO();
        }

        ProjectMonthlyReportVO projectMonthlyReportVO = Pmr2VO.convert(projectMonthlyReport);

        return projectMonthlyReportVO;
    }

    @Override
    public List<ProjectMonthlyReportVO> findAll() {

        List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper.selectAll();

        if (projectMonthlyReports == null) {
            log.error("【月报】 查找全部月报时，查询记录为空");
            return new ArrayList();
        }

        List<ProjectMonthlyReportVO> projectMonthlyReportVOs = projectMonthlyReports.stream().map(e -> Pmr2VO.convert(e))
                .collect(Collectors.toList());

        return projectMonthlyReportVOs;
    }

    /**
     * 查询当前用户的在某一时间段内所有的月报记录
     * 如果是管理员 必须传baseInfoId进来查找特定水库的月报
     * @param startDate 2018-08-01
     * @param endDate
     * @return
     */
    @Override
    public List<ProjectMonthlyReportVO> findByBaseInfoIdAndPeriodWithImg(String baseInfoId, String startDate, String endDate) {

        if (startDate == null || "".equals(startDate)) {
            startDate = ProjectMonthlyReportMapper.DEFAULT_START_DATE;
        }

        if (endDate == null || "".equals(endDate)) {
            endDate = new Date().toString();
        }

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 如果是普通用户， 则只能查询自身的水库对应月报
        if (RoleCheckUtil.checkIfPossessARole(thisUser, RoleEnum.PLP.getKey())) {
            baseInfoId = thisUser.getBaseInfoId();
            if (baseInfoId == null || "".equals(baseInfoId)) {
                log.error("【月报】 查找月报时， 用户的水库基础信息缺失，无法查找归属某一水库的项目月报");
                throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
            }
            // 如果是管理员则查询当前要查询的水库baseInfoId是否在其辖区内
        } else {
            Region region = thisUser.getRegion();
            if (region == null) {
                log.error("【月报】 查找月报时，管理员的辖区信息为空");
                throw new SysException(SysEnum.NOT_EXIST_RECORD);
            }
            List<Region> leafRegions = regionService.findLeafRecursive(region.getRegionId());
            List<BaseInfo> baseInfos = baseInfoMapper.findByRegionsIn(leafRegions);
            if (baseInfos == null || baseInfos.size() == 0) {
                log.error("【月报】 查找月报时，管理员辖区内无水库");
                throw new SysException(SysEnum.NOT_EXIST_RECORD);
            }
            List<String> baseInfoIds = baseInfos.stream().map(e -> e.getBaseInfoId()).collect(Collectors.toList());
            if (!baseInfoIds.contains(baseInfoId)) {
                log.error("【月报】 查找月报时，管理员查询的水库不在辖区内");
                throw new SysException(SysEnum.ILLEGAL_OPERATION);
            }
        }

        List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper
                .findByBaseInfoIdAndPeriodWithImg(baseInfoId, startDate, endDate);

        if (projectMonthlyReports == null) {
            log.error("【月报】 查找月报时，查询记录为空");
            return new ArrayList<>();
        }
        List<ProjectMonthlyReportVO> projectMonthlyReportVOs = projectMonthlyReports.stream().map(e -> Pmr2VO.convert(e))
                .collect(Collectors.toList());

        return projectMonthlyReportVOs;
    }

    /**
     * 查询当前用户的所有月报和图片
     * @return
     */
    @Override
    public List<ProjectMonthlyReportVO> findByBaseInfoIdWithImg() {

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (thisUser.getBaseInfoId() == null || "".equals(thisUser.getBaseInfoId())) {
            log.error("【月报】 查找月报时， 用户的水库基础信息缺失，无法查找归属某一水库的项目月报");
            throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
        }

        List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper.findByBaseInfoIdWithImg(thisUser.getBaseInfoId());

        if (projectMonthlyReports == null) {
            log.error("【月报】 查找月报时，查询记录为空");
            return new ArrayList<>();
        }
        List<ProjectMonthlyReportVO> projectMonthlyReportVOs = projectMonthlyReports.stream().map(e -> Pmr2VO.convert(e))
                .collect(Collectors.toList());

        return projectMonthlyReportVOs;
    }


    /**
     * 根据登陆用户的水库信息获取该其所有月报信息
     * 最高级用户才能向baseInfoId里传值
     * 按时间区间
     * @return
     */
    @Override
    public List<ProjectMonthlyReport> findAllPmrByBaseInfoIdByTimeDuration(String baseInfoId, String startDate, String endDate) {

        if (startDate == null || "".equals(startDate)) {
            startDate = ProjectMonthlyReportMapper.DEFAULT_START_DATE;
        }

        if (endDate == null || "".equals(endDate)) {
            endDate = new Date().toString();
        }

        if (baseInfoId == null || "".equals(baseInfoId)) {
            log.error("【月报】 通过baseinfoId和时间区间查找月报时，baseInfoId为空");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 如果是普通用户
        if (RoleCheckUtil.checkIfPossessARole(userInfo, "ROLE_PLP")) {
            baseInfoId = userInfo.getBaseInfoId();
        } else {
            Region region = userInfo.getRegion();
            List<Region> leafRegion = regionService.findLeafRecursive(region.getRegionId());
            List<BaseInfo> baseInfos = baseInfoMapper.findByRegionsIn(leafRegion);
            List<String> baseInfoIds = baseInfos.stream().map(e -> e.getBaseInfoId()).collect(Collectors.toList());
            if (!baseInfoIds.contains(baseInfoId)) {
                log.error("【月报】 通过baseInfoId查找水库工程月报时，水库工程不在辖区内！");
                throw new SysException(SysEnum.ILLEGAL_OPERATION);
            }
        }

        List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper.findByBaseInfoIdAndPeriodWithImg(baseInfoId, startDate, endDate);

        return projectMonthlyReports;
    }

    /**
     * 计算登陆用户所在区域所有工程的投资完成情况
     * 三个参数为选填 startDate 默认从2000年开始 endDate默认为当前时间 regionId默认为0 即查询自身区域内
     * 最高级用户 即 ROLE_PROVINCE 可以使用非0 regionId 参数 以查询某个特定区域的情况
     * @return
     */
    /**
     * 计算登陆用户所在区域所有工程的投资完成情况
     * 三个参数为选填 startDate 默认从2000年开始 endDate默认为当前时间 regionId默认为0 即查询自身区域内
     * 管理员用户可以传入baseInfoId查询自己辖区内某个水库的情况
     * @return
     */
    @Override
    public BigDecimal calcOverallInvestmentCompletion(String baseInfoId, int regionId, String startDate, String endDate, String by) {

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ProjectMonthlyReportVO> projectMonthlyReportVOs = Collections.EMPTY_LIST;

        switch (by) {
            case "region":
                projectMonthlyReportVOs = findAllPmrViaUserRegionByTimeDuration(userInfo, regionId, startDate, endDate);
                break;
            case "baseInfo":
                projectMonthlyReportVOs = findByBaseInfoIdAndPeriodWithImg(baseInfoId, startDate, endDate);
                break;
            default:
                log.error("【月报】 调用查询投资完成总和时出错，没有明确调用方法。");
                throw new SysException(SysEnum.METHOD_CALL_ERROR);
        }

        BigDecimal result = PmrCalculator.calOverallInvestmentCompletion(projectMonthlyReportVOs);

        return result;
}


   /* @Override
    public BigDecimal calcOverallInvestmentCompletionByRegionId(int regionId, String startDate, String endDate) {

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ProjectMonthlyReportVO> projectMonthlyReportVOs = Collections.EMPTY_LIST;

        projectMonthlyReportVOs = findAllPmrViaUserRegionByTimeDuration(userInfo, regionId, startDate, endDate);

        BigDecimal result = PmrCalculator.calOverallInvestmentCompletion(projectMonthlyReportVOs);

        return result;
    }*/

    /**
     * 根据登陆用户所属地区获取该区域下的所有月报信息
     * 只有最高级用户才能向regionId里传非0值 如果高级管理员不传值 则为自己的region
     * 如果是其他管理员则只能用自己的region
     * 按时间区间
     * @return List<ProjectMonthlyReport> region下的所有月报信息
     */
    @Override
    public List<ProjectMonthlyReportVO> findAllPmrViaUserRegionByTimeDuration(UserInfo userInfo, int regionId, String startDate, String endDate) {

        List<ProjectMonthlyReportVO> projectMonthlyReportVOs = null;

        if (startDate == null || "".equals(startDate)) {
            startDate = ProjectMonthlyReportMapper.DEFAULT_START_DATE;
        }

        Region region = null;

        // 如果是业主
        if (RoleCheckUtil.checkIfPossessARole(userInfo, RoleEnum.PLP.getKey())) {
            log.error("【月报】 没有权限作此操作");
            throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
            // 如果是最高管理员
        } else if (RoleCheckUtil.checkIfPossessARole(userInfo, RoleEnum.PROVINCE.getKey())){
            if (regionId == 0) {
                region = userInfo.getRegion();
            } else {
                region = regionService.findByRegionId(regionId);
            }
            // 如果是其他管理员
        } else {
            region = userInfo.getRegion();
        }

        if (region == null) {
            log.error("【月报】 在获取用户所在区域所有月报信息时，用户区域或查询区域信息为空");
            return new ArrayList<>();
        }

        List<ProjectMonthlyReport> projectMonthlyReports = findByRegionDuring(startDate, endDate, region);

        if (projectMonthlyReports == null || projectMonthlyReports.size() ==0) {
            projectMonthlyReportVOs = Collections.EMPTY_LIST;
        } else {
            projectMonthlyReportVOs = projectMonthlyReports.stream()
                    .map(e -> Pmr2VO.convert(e)).collect(Collectors.toList());
        }

        return projectMonthlyReportVOs;
    }

    /**
     * 根据时间区间 查找某区域的所有水库 在特定时间区间内的 所有月报
     * @param startDate
     * @param endDate
     * @return
     */
    public List<ProjectMonthlyReport> findByRegionDuring(String startDate, String endDate, Region region) {

        if (startDate == null || "".equals(startDate)) {
            startDate = ProjectMonthlyReportMapper.DEFAULT_START_DATE;
        }

        int thisUserRegionId = region.getRegionId();

        List<Region> leafRegions = regionService.findLeafRecursive(thisUserRegionId);

        List<BaseInfo> baseInfos = baseInfoMapper.findByRegionsIn(leafRegions);
        List<String> baseInfoIds = baseInfos.stream().map(e -> e.getBaseInfoId()).collect(Collectors.toList());

        List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper.findByBaseInfoIdsInWithImgDuring(startDate, endDate, baseInfoIds);

        return projectMonthlyReports;
    }


    @Override
    public void syncToRedis() {

        List<ProjectMonthlyReport> projectMonthlyReport = pmrMapper.selectAll();
        List<ProjectMonthlyReportVO> projectMonthlyReportVOs = projectMonthlyReport.stream().map(e -> Pmr2VO.convert(e))
                .collect(Collectors.toList());

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String projectMonthlyReportVOsStr = objectMapper.writeValueAsString(projectMonthlyReportVOs);
                stringRedisTemplate.opsForValue().set(RedisKeys.ALLPMR.getKey(), projectMonthlyReportVOsStr);
            } catch (JsonProcessingException e) {
                log.error("月报信息同步至redis的过程中， 转化json出错。");
                return;
            }
        });
        log.error("月报信息同步至redis完成。");
    }


}
