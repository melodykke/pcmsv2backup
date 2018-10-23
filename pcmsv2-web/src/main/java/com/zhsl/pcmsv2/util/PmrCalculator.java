package com.zhsl.pcmsv2.util;

import com.zhsl.pcmsv2.model.ProjectMonthlyReport;

import java.math.BigDecimal;
import java.util.List;

public class PmrCalculator {

    // 计算总的完成投资 （按概算构成分）
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
                BigDecimal resettlementArrangement = pmr.getResettlementArrangement();
                BigDecimal waterConservation = pmr.getWaterConservation();
                BigDecimal environmentalProtection = pmr.getEnvironmentalProtection();
                BigDecimal otherCost = pmr.getOtherCost();

                BigDecimal singlePmr = civilEngineering.add(metalMechanism.add(independentCost
                        .add(electromechanicalEquipment.add(temporaryWork.add(resettlementArrangement
                                .add(waterConservation.add(environmentalProtection.add(otherCost))))))));
                result = result.add(singlePmr);
            }
        }
        return result;
    }

    // 计算总到位资金 （中央、省、地方自筹）
    public static BigDecimal calOverallAvailableInvestment(List<ProjectMonthlyReport> projectMonthlyReportList) {

        BigDecimal result = new BigDecimal(0);

        if (projectMonthlyReportList.size() == 0) {
            return result;
        } else {
            for (ProjectMonthlyReport pmr : projectMonthlyReportList) {

                BigDecimal availableCentralInvestment = pmr.getAvailableCentralInvestment();
                BigDecimal availableLocalInvestment = pmr.getAvailableLocalInvestment();
                BigDecimal availableProvincialInvestment = pmr.getAvailableProvincialInvestment();

                BigDecimal singlePmr = availableCentralInvestment.add(availableLocalInvestment.
                        add(availableProvincialInvestment));
                result = result.add(singlePmr);
            }
        }
        return result;
    }



}
