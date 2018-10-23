package com.zhsl.pcmsv2.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.convertor.dto2model.BaseInfoDTO2Model;
import com.zhsl.pcmsv2.convertor.tovo.BaseInfo2VO;
import com.zhsl.pcmsv2.dto.BaseInfoDTO;
import com.zhsl.pcmsv2.enums.RedisKeys;
import com.zhsl.pcmsv2.enums.RoleEnum;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.mapper.BaseInfoMapper;
import com.zhsl.pcmsv2.mapper.PlantStateMapper;
import com.zhsl.pcmsv2.mapper.ProjectMonthlyReportMapper;
import com.zhsl.pcmsv2.model.*;
import com.zhsl.pcmsv2.service.BaseInfoService;
import com.zhsl.pcmsv2.service.MonthReportService;
import com.zhsl.pcmsv2.service.RegionService;
import com.zhsl.pcmsv2.util.*;
import com.zhsl.pcmsv2.vo.BaseInfoVO;
import com.zhsl.pcmsv2.vo.LifeCircle;
import com.zhsl.pcmsv2.vo.ProjectMonthlyReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class BaseInfoServiceImpl implements BaseInfoService {

    @Autowired
    private BaseInfoMapper baseInfoMapper;

    @Autowired
    private RegionService regionService;

    @Autowired
    private PlantStateMapper plantStateMapper;

    @Autowired
    private ProjectMonthlyReportMapper pmrMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 删除水库
     *
     * @param baseInfoId
     * @return
     */
    @Override
    public int delete(String baseInfoId) {
        int result = baseInfoMapper.deleteByPrimaryKey(baseInfoId);
        return result;
    }

    @Override
    public int modify(BaseInfoDTO baseInfoDTO) {

        if (baseInfoDTO == null) {
            log.error("【基础信息】 修改水库基础信息时，收到数据 baseInfoDTO 为空");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        if (baseInfoDTO.getBaseInfoId() == null || "".equals(baseInfoDTO.getBaseInfoId())) {
            log.error("【基础信息】 修改水库基础信息时，收到数据 baseInfoDTO ID为空");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }
        // 从数据库拿出记录
        BaseInfo baseInfo = baseInfoMapper.selectByPrimaryKey(baseInfoDTO.getBaseInfoId());
        // 保留ID 和 名称
        String baseInfoId = baseInfo.getBaseInfoId();
        String plantName = baseInfo.getPlantName();

        BeanUtils.copyProperties(baseInfoDTO, baseInfo);

        // 谁修改的 owner就是谁
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        baseInfo.setBaseInfoId(baseInfoId);
        baseInfo.setPlantName(plantName);
        baseInfo.setUpdateTime(new Date());
        baseInfo.setOwner(userInfo.getUsername());

        int result = baseInfoMapper.updateByPrimaryKey(baseInfo);

        return result;
    }

    /**
     * 查询所有水库的基础信息， 如果没有则返回空的容器。
     *
     * @return
     */
    @Override
    public List<BaseInfoVO> findAll() {

        // 首先尝试从redis中获取数据
        if (stringRedisTemplate.hasKey(RedisKeys.ALLBASEINFO.getKey())) {
            String allBaseInfoVOsStr = stringRedisTemplate.opsForValue().get(RedisKeys.ALLBASEINFO.getKey());
            if (allBaseInfoVOsStr != null && !"".equals(allBaseInfoVOsStr)) {
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, BaseInfoVO.class);
                try {
                    List<BaseInfoVO> baseInfoVOs = objectMapper.readValue(allBaseInfoVOsStr, javaType);
                    return baseInfoVOs;
                } catch (IOException e) {
                    log.error("【用户】 查询所有水库基础信息时， 从redis中获取的数据在json转化为对象时出错");
                }
            }
        }

        // 如果从redis中获取失败， 则尝试从数据库中获取数据
        List<BaseInfo> baseInfos = baseInfoMapper.selectAll();

        if (baseInfos == null) {
            log.info("【查询所有项目基础信息时，没有查找到任何记录】");
            return new ArrayList<>();
        }

        List<BaseInfoVO> baseInfoVOs = baseInfos.stream().map(e -> BaseInfo2VO.convert(e)).collect(Collectors.toList());

        return baseInfoVOs;
    }


    @Override
    public int create(BaseInfoDTO baseInfoDTO) {

        if (baseInfoDTO == null) {
            log.error("【基础信息】 创建水库基础信息时，收到的基础信息baseInfoDTO为空");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        List<String> allPlantName = baseInfoMapper.findAllPlantName();

        if (allPlantName.contains(baseInfoDTO.getPlantName())) {
            log.error("【基础信息】 创建水库基础信息时，收到的基础信息baseInfoDTO中的水库名字plantName在系统中已存在，" +
                    "不能重复申请 baseInfoDTO：", baseInfoDTO);
            throw new SysException(SysEnum.DUPLICATED_RECORD);
        }

        BaseInfo baseInfo = BaseInfoDTO2Model.convert(baseInfoDTO);

        // 项目法人 如果用户端提交过来法人名字则用，否则设置为信息提交人的账号名
        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (baseInfo.getOwner() == null || "".equals(baseInfo.getOwner())) {
            baseInfo.setOwner(thisUser.getUsername());
        }

        baseInfo.setBaseInfoId(UUIDUtils.getUUID());
        baseInfo.setPlantState(new PlantState(1));
        baseInfo.setCreateTime(new Date());
        baseInfo.setUpdateTime(new Date());

        int result = baseInfoMapper.insert(baseInfo);

        return result;
    }

    /**
     * 按照ID查找水库基础信息
     *
     * @param baseInfoId
     * @return
     */
    @Override
    public BaseInfoVO findById(String baseInfoId) {

        BaseInfo baseInfo = baseInfoMapper.selectByPrimaryKey(baseInfoId);

        if (baseInfo == null) {
            log.info("【查询某个项目基础信息时，没有查找到任何记录】");
            return new BaseInfoVO();
        }

        BaseInfoVO baseInfoVO = BaseInfo2VO.convert(baseInfo);

        return baseInfoVO;
    }

    @Override
    public List<BaseInfoVO> findByRegionId(HttpServletRequest request) {

        Region region = null;

        int regionId = 0;

        try {
            regionId = ServletRequestUtils.getIntParameter(request, "regionId")==null? 0 :
                    ServletRequestUtils.getIntParameter(request, "regionId");
        } catch (ServletRequestBindingException e) {
            log.info("【基础信息】 通过regionId查找区域水库基本信息时，没有接收到regionId参数");
        }

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (regionId == 0) {
            region = thisUser.getRegion();
        } else if (regionId > 0) {
            if (RoleCheckUtil.checkIfPossessARole(thisUser, RoleEnum.PROVINCE.getKey())) {
                region = regionService.findByRegionId(regionId);

            }
        }

        if (region == null) {
            log.error("【基础信息】 按区域查询项目基础信息时，作查询操作的用户没有有效的区域信息");
            throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
        }
        List<Region> childrenRegion = regionService.findChildrenRecursive(region.getRegionId());
        List<BaseInfo> baseInfos = baseInfoMapper.findByRegionsIn(childrenRegion);

        if (baseInfos == null) {

            return new ArrayList<>();
        }

        List<BaseInfoVO> baseInfoVOs = baseInfos.stream().map(e -> BaseInfo2VO.convert(e)).collect(Collectors.toList());

        return baseInfoVOs;
    }

    @Override
    public int updateCommenceDate(Date commenceDate) {

        if (commenceDate == null) {
            log.error("【基础信息】 更新水库开工时间时，用户传入的commenceDate不正确！");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (thisUser.getBaseInfoId() == null || "".equals(thisUser.getBaseInfoId())) {
            log.error("【基础信息】 更新水库开工时间时，用户的水库信息不存在！");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }

        BaseInfo baseInfo = baseInfoMapper.selectByPrimaryKey(thisUser.getBaseInfoId());
        if (baseInfo == null) {
            log.error("【基础信息】 更新水库开工时间时，用户的水库信息不存在！");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }
        PlantState plantState = baseInfo.getPlantState();
        if (plantState == null) {
            log.error("【基础信息】 更新水库开工时间时，用户的水库的节点状态信息不存在！");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }
        if (plantState.getStateId() <= 2) {
            log.error("【基础信息】 更新水库开工时间时，用户的水库的节点状态不在建设期，不能更新开工时间！");
            throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD.getCode(), "水库的节点状态不在建设期，" +
                    "请更新水库状态节点，然后再试！");
        }

        int result = baseInfoMapper.updateCommenceDate(thisUser.getBaseInfoId(), commenceDate);

        return result;
    }

    @Override
    public List<LifeCircle> buildLifeCircle() {

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<LifeCircle> lifeCircles = new ArrayList<>();

        // 如果登录用户是业主时
        if (RoleCheckUtil.checkIfPossessARole(thisUser, RoleEnum.PLP.getKey())) {
            if (thisUser.getBaseInfoId() == null) {
                log.error("【生命周期】 在获取生命周期相关信息时，业主用户的水库信息为空");
                throw new SysException(SysEnum.NOT_EXIST_RECORD);
            }
            BaseInfo baseInfo = baseInfoMapper.selectByPrimaryKey(thisUser.getBaseInfoId());
            if (baseInfo == null) {
                log.error("【生命周期】 在获取生命周期相关信息时，业主用户的水库信息为空");
                throw new SysException(SysEnum.NOT_EXIST_RECORD);
            }

            // 建设周期（月）->(天)
            BigDecimal timeLimitDay = baseInfo.getTimeLimit().multiply(new BigDecimal(30));
            // 到目前为止的实际用时（月）

            // 计算当前日期和开工日期之间的天数差值
            int differDays = CalendarUtil.calDifferDays(baseInfo.getCommenceDate());
            BigDecimal actualLimit = new BigDecimal(differDays).setScale(2, BigDecimal.ROUND_HALF_UP);


            // 工程总投资
            BigDecimal totalInvestment = baseInfo.getTotalInvestment();
            // 到目前为止的投资完成 、到目前为止的资金到位
            BigDecimal investmentSofar = null;
            BigDecimal availableInvestmentSofar = null;

            List<ProjectMonthlyReport> projectMonthlyReports = pmrMapper.findByBaseInfoIdWithImg(baseInfo.getBaseInfoId());

            if (projectMonthlyReports == null || projectMonthlyReports.size() == 0){
                investmentSofar = new BigDecimal(0.00);
                availableInvestmentSofar = new BigDecimal(0.00);
            } else {
                investmentSofar = PmrCalculator.calOverallInvestmentCompletion(projectMonthlyReports);
                availableInvestmentSofar = PmrCalculator.calOverallAvailableInvestment(projectMonthlyReports);
            }

            LifeCircle lifeCircle = new LifeCircle(timeLimitDay, actualLimit, totalInvestment,
                    investmentSofar, availableInvestmentSofar);

            lifeCircles.add(lifeCircle);
        }

        return lifeCircles;
    }

    @Override
    public Feedback approveBaseInfo(UserInfo thisUser, Boolean switchState, String checkinfo, String baseInfoId) {
        return null;
    }

    /**
     * 将数据库中的所有baseInfo信息缓存至redis中 key：allBaseInfo
     */
    public void syncToRedis() {

        List<BaseInfo> baseInfos = baseInfoMapper.selectAll();
        List<BaseInfoVO> baseInfoVOs = baseInfos.stream().map(e -> BaseInfo2VO.convert(e)).collect(Collectors.toList());

        // 如果成功提交一个新的水库信息，则更新redis里所有水库的数据
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String allBaseInfoVOsStr = objectMapper.writeValueAsString(baseInfoVOs);
                stringRedisTemplate.opsForValue().set(RedisKeys.ALLBASEINFO.getKey(), allBaseInfoVOsStr);
            } catch (JsonProcessingException e) {
                log.error("水库基础信息同步至redis的过程中， 转化json出错。");
                return;
            }
        });
        log.error("水库基础信息同步至redis完成。");
    }
}
