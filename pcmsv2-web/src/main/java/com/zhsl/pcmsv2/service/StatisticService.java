package com.zhsl.pcmsv2.service;

import com.zhsl.pcmsv2.model.Region;
import com.zhsl.pcmsv2.vo.RegionInvestmentVO;

import javax.servlet.http.HttpServletRequest;

public interface StatisticService {

    /**
     * 根据region 计算此区域内水库的总投资
     * 投资完成 和 资金到位的情况
     * 此方法只能管理员调用
     * @param regionId
     * @return RegionInvestmentVO
     */
    RegionInvestmentVO calcRegionInvestmentStatistic(HttpServletRequest request);

}
