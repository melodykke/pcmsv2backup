package com.zhsl.pcmsv2.service.impl;

import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.enums.RoleEnum;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.mapper.RegionMapper;
import com.zhsl.pcmsv2.model.BaseInfo;
import com.zhsl.pcmsv2.model.Region;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.service.BaseInfoService;
import com.zhsl.pcmsv2.service.MonthReportService;
import com.zhsl.pcmsv2.service.RegionService;
import com.zhsl.pcmsv2.service.StatisticService;
import com.zhsl.pcmsv2.util.BaseInfoCalculator;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.util.RoleCheckUtil;
import com.zhsl.pcmsv2.vo.RegionInvestmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private RegionService regionService;

    @Autowired
    private BaseInfoService baseInfoService;

    @Autowired
    private MonthReportService monthReportService;


    /**
     * 根据region 计算此区域内水库的总投资
     * 投资完成 和 资金到位的情况
     * 此方法只能管理员调用
     * @param request
     * @return RegionInvestmentVO
     */
    @Override
    public RegionInvestmentVO calcRegionInvestmentStatistic(HttpServletRequest request) {
        int regionId = 0;
        String startDate = "";
        String endDate = "";

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Region region = userInfo.getRegion();
        if (region == null) {
            log.error("【数据处理】 数据处理时出错，调用calcRegionInvestmentStatistic方法人员的管辖区域出错！");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }
        try {
            regionId = ServletRequestUtils.getIntParameter(request, "regionId") == null ? region.getRegionId() :
                    ServletRequestUtils.getIntParameter(request, "regionId");
            startDate = ServletRequestUtils.getStringParameter(request, "startDate");
            endDate = ServletRequestUtils.getStringParameter(request, "endDate");
        } catch (ServletRequestBindingException e) {
           log.info("【数据处理】 数据处理时，没有接收到指定参数，系统采用默认值");
        }

        if (regionId == 0) {
            log.error("【数据处理】 数据处理时出错，调用calcRegionInvestmentStatistic方法人员不具备权限，非法操作！");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        if (!RoleCheckUtil.checkIfPossessARole(userInfo, RoleEnum.PLP.getKey())) {

            Region myRegion = userInfo.getRegion();
            if (myRegion == null) {
                log.error("【数据处理】 数据处理时出错，调用calcRegionInvestmentStatistic时，获取用户所在区域信息时出错！");
                throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
            }
            int myRegionId = myRegion.getRegionId();

            List<Region> nodeRegions = regionService.findNodeRecursive(myRegionId);
            if (nodeRegions == null || nodeRegions.size() == 0) {
                log.error("【数据处理】 数据处理时出错，调用calcRegionInvestmentStatistic时，获取用户管辖区域信息时出错！");
                throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
            }
            List<Integer> nodeRegionIds = nodeRegions.stream().map(e -> e.getRegionId()).collect(Collectors.toList());
            // 检查传入查询的regionId是否是调用本方法的用户管辖的
            if (!nodeRegionIds.contains(regionId)) {
                log.error("【数据处理】 数据处理时出错，调用calcRegionInvestmentStatistic方法人员不具备权限，" +
                        "不能查询不属于自己辖区的情况，非法操作！");
                throw new SysException(SysEnum.ILLEGAL_OPERATION);
            }

            // 如果以上检查都OK的话，正式进入以下计算逻辑
            RegionInvestmentVO rootRI = buildRegionInvestment(regionId, startDate, endDate);

            List<Region> subRegions = regionService.findSubRegions(regionId);
            if (subRegions != null && subRegions.size() >0) {

                List<RegionInvestmentVO> subRegionInvestmentVOs = new ArrayList<>();

                for (Region nodeRegion : subRegions) {
                    RegionInvestmentVO nodeRI = buildRegionInvestment(nodeRegion.getRegionId(), startDate, endDate);
                    subRegionInvestmentVOs.add(nodeRI);
                }

                rootRI.setChildren(subRegionInvestmentVOs);
            }

            return rootRI;

        }
        return null;
    }

    protected RegionInvestmentVO buildRegionInvestment(int regionId, String startDate, String endDate) {

        if (endDate == null || "".equals(endDate)) {
            endDate = new Date().toString();
        }

        RegionInvestmentVO regionInvestmentVO = new RegionInvestmentVO();
        Region region = regionService.findByRegionId(regionId);

        // 赋值区域信息
        BeanUtils.copyProperties(region, regionInvestmentVO);

        // 计算总投资
        List<Region> leafRegions = regionService.findLeafRecursive(regionId);
        List<BaseInfo> baseInfos = baseInfoService.findByRegionIdsIn(leafRegions);
        BigDecimal totalInvestment = BaseInfoCalculator.calcTotalInvestment(baseInfos);
        regionInvestmentVO.setTotalInvestment(totalInvestment);

        // 计算到某个时间点的投资完成
        BigDecimal investmentSofar = monthReportService.calcOverallInvestmentCompletion(null, regionId, startDate, endDate, "region");
        regionInvestmentVO.setInvestmentSofar(investmentSofar);

        // 计算到某个时间点的到位资金
        BigDecimal investmentAvailableSofar = monthReportService.calcOverallInvestmentAvailable(null, regionId, startDate, endDate, "region");
        regionInvestmentVO.setAvailableInvestmentSofar(investmentAvailableSofar);

        return regionInvestmentVO;
    }



}
