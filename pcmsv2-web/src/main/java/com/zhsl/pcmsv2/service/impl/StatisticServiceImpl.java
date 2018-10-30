package com.zhsl.pcmsv2.service.impl;

import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.enums.RoleEnum;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.mapper.RegionMapper;
import com.zhsl.pcmsv2.model.Region;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.service.RegionService;
import com.zhsl.pcmsv2.service.StatisticService;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.util.RoleCheckUtil;
import com.zhsl.pcmsv2.vo.RegionInvestmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private RegionService regionService;


    /**
     * 根据region 计算此区域内水库的总投资
     * 投资完成 和 资金到位的情况
     * 此方法只能管理员调用
     * @param regionId
     * @return RegionInvestmentVO
     */
    @Override
    public RegionInvestmentVO calcRegionInvestmentStatistic(int regionId) {

        if (regionId == 0) {
            log.error("【数据处理】 数据处理时出错，调用calcRegionInvestmentStatistic方法人员不具备权限，非法操作！");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
            RegionInvestmentVO rootRI = buildRegionInvestment(regionId);

            List<Region> subRegions = regionService.findSubRegions(regionId);
            if (subRegions != null && subRegions.size() >0) {

                List<RegionInvestmentVO> subRegionInvestmentVOs = new ArrayList<>();

                for (Region nodeRegion : subRegions) {
                    RegionInvestmentVO nodeRI = buildRegionInvestment(nodeRegion.getRegionId());
                    subRegionInvestmentVOs.add(nodeRI);
                }

                rootRI.setChildren(subRegionInvestmentVOs);
            }

            return rootRI;

        }
        return null;
    }

    protected RegionInvestmentVO buildRegionInvestment(int regionId) {

        RegionInvestmentVO regionInvestmentVO = new RegionInvestmentVO();
        Region region = regionService.findByRegionId(regionId);

        BeanUtils.copyProperties(region, regionInvestmentVO);

        regionInvestmentVO.setTotalInvestment(null);
        regionInvestmentVO.setInvestmentSofar(null);
        regionInvestmentVO.setAvailableInvestmentSofar(null);

        return regionInvestmentVO;
    }



}
