package com.zhsl.pcmsv2.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.convertor.dto2model.PmrDTO2Model;
import com.zhsl.pcmsv2.convertor.tovo.Pmr2VO;
import com.zhsl.pcmsv2.dto.ProjectMonthlyReportDTO;
import com.zhsl.pcmsv2.enums.RedisKeys;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
     * @param startDate 2018-08-01
     * @param endDate
     * @return
     */
    @Override
    public List<ProjectMonthlyReportVO> findByBaseInfoIdAndPeriodWithImg(String startDate, String endDate) {

        if (startDate == null || "".equals(startDate)) {
            log.error("【月报】 区间区间查找月报时， 时间参数为空");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        if (endDate == null || "".equals(endDate)) {
            endDate = new Date().toString();
        }

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (thisUser.getBaseInfoId() == null || "".equals(thisUser.getBaseInfoId())) {
            log.error("【月报】 查找月报时， 用户的水库基础信息缺失，无法查找归属某一水库的项目月报");
            throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
        }

        List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper
                .findByBaseInfoIdAndPeriodWithImg(thisUser.getBaseInfoId(), startDate, endDate);
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
     * 根据登陆用户所属地区获取该区域下的所有月报信息
     * 只有最高级用户才能向regionId里传非0值
     * 按时间区间
     * @return
     */
    @Override
    public List<ProjectMonthlyReport> findAllPmrViaUserRegionByTimeDuration(int regionId, String startDate, String endDate) {

        Region region = null;

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (regionId == 0) {
            if (thisUser == null) {
                log.error("【月报】 在获取用户所在区域所有月报信息时，用户未登录或身份信息有误");
                return new ArrayList<>();
            }
            region = thisUser.getRegion();
        } else if (regionId > 0){
            if (RoleCheckUtil.checkIfPossessProvinceRole(thisUser)) {
                region = regionService.findByRegionId(regionId);
            } else {
                log.error("【月报】 没有权限作此操作");
                throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
            }
        }

        if (region == null) {
            log.error("【月报】 在获取用户所在区域所有月报信息时，用户区域或查询区域信息为空");
            return new ArrayList<>();
        }

        List<ProjectMonthlyReport> projectMonthlyReports = findByRegionDuring(startDate, endDate, region);

        return projectMonthlyReports;
    }

    /**
     * 计算登陆用户所在区域所有工程的投资完成情况
     * 三个参数为选填 startDate 默认从2000年开始 endDate默认为当前时间 regionId默认为0 即查询自身区域内
     * 最高级用户 即 ROLE_PROVINCE 可以使用非0 regionId 参数 以查询某个特定区域的情况
     * @return
     */
    @Override
    public BigDecimal calcOverallInvestmentCompletion(HttpServletRequest request) {
        String startDate = "";
        String endDate = "";
        int regionId = 0;
        try {
            startDate = ServletRequestUtils.getStringParameter(request,"startDate");
            endDate = ServletRequestUtils.getStringParameter(request, "endDate");
            regionId = ServletRequestUtils.getIntParameter(request, "regionId")==null? 0 :
                    ServletRequestUtils.getIntParameter(request, "regionId");
        } catch (ServletRequestBindingException e) {
            log.error("【月报】 计算用户区域内所有工程总投资完成时， 用户端传来的startDate或endDate参数接收出错，程序将采用默认值");
        }

        List<ProjectMonthlyReport> projectMonthlyReports = findAllPmrViaUserRegionByTimeDuration(regionId, startDate, endDate);

        BigDecimal result = PmrCalculator.calOverallInvestmentCompletion(projectMonthlyReports);

        return result;
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

        List<Region> leafRegions = regionService.findChildrenRecursive(thisUserRegionId);

        List<BaseInfo> baseInfos = baseInfoMapper.findByRegionsIn(leafRegions);
        List<String> baseInfoIds = baseInfos.stream().map(e -> e.getBaseInfoId()).collect(Collectors.toList());

        List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper.findByBaseInfoIdsInWithImgDuring(startDate, endDate, baseInfoIds);

        return projectMonthlyReports;
    }
}
