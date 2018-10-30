package com.zhsl.pcmsv2.util;

import com.zhsl.pcmsv2.model.BaseInfo;

import java.math.BigDecimal;
import java.util.List;

public class BaseInfoCalculator {

    /**
     * 计算总投资
     * @param baseInfos
     * @return
     */
    public static BigDecimal calcTotalInvestment(List<BaseInfo> baseInfos) {

        BigDecimal totalInvenstment = BigDecimal.ZERO;

        for (BaseInfo baseInfo : baseInfos) {
            totalInvenstment = totalInvenstment.add(baseInfo.getTotalInvestment());
        }

        return totalInvenstment;
    }
}
