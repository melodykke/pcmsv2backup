package com.zhsl.pcmsv2.util;

import com.zhsl.pcmsv2.model.ProjectMonthlyReport;

import java.math.BigDecimal;
import java.util.List;

public class PmrCalculator {

    // 计算总的工程部分投资完成
    public static BigDecimal calOverallInvestmentCompletion(List<ProjectMonthlyReport> projectMonthlyReportList) {

        BigDecimal result = new BigDecimal(0);

        if (projectMonthlyReportList.size() == 0) {
            return result;
        } else {
            for (ProjectMonthlyReport pmr : projectMonthlyReportList) {

                BigDecimal civilEngineering = pmr.getCivilEngineering();
                BigDecimal metalMechanism = pmr.getMetalMechanism();
                BigDecimal independentCost = pmr.getIndependentCost();
                BigDecimal electromechanicalEquipment = pmr.getElectromechanicalEquipment();
                BigDecimal temporaryWork = pmr.getTemporaryWork();

                BigDecimal singlePmr = civilEngineering.add(metalMechanism.add(independentCost
                        .add(electromechanicalEquipment.add(temporaryWork))));
                result = result.add(singlePmr);
            }
        }
        return result;
    }



}
