package com.zhsl.pcmsv2.util;

import com.zhsl.pcmsv2.model.ProjectMonthlyReport;
import com.zhsl.pcmsv2.vo.ProjectMonthlyReportVO;

import java.math.BigDecimal;
import java.util.List;

public class PmrCalculator {

    // 计算总的完成投资 （按概算构成分）
    public static BigDecimal calOverallInvestmentCompletion(List<ProjectMonthlyReportVO> projectMonthlyReportVOs) {

        BigDecimal result = new BigDecimal(0.00);

        if (projectMonthlyReportVOs.size() == 0) {
            return result;
        } else {
            for (ProjectMonthlyReportVO pmrVO : projectMonthlyReportVOs) {

                BigDecimal civilEngineering = pmrVO.getCivilEngineering();
                BigDecimal metalMechanism = pmrVO.getMetalMechanism();
                BigDecimal independentCost = pmrVO.getIndependentCost();
                BigDecimal electromechanicalEquipment = pmrVO.getElectromechanicalEquipment();
                BigDecimal temporaryWork = pmrVO.getTemporaryWork();
                BigDecimal resettlementArrangement = pmrVO.getResettlementArrangement();
                BigDecimal waterConservation = pmrVO.getWaterConservation();
                BigDecimal environmentalProtection = pmrVO.getEnvironmentalProtection();
                BigDecimal otherCost = pmrVO.getOtherCost();

                BigDecimal singlePmr = civilEngineering.add(metalMechanism.add(independentCost
                        .add(electromechanicalEquipment.add(temporaryWork.add(resettlementArrangement
                                .add(waterConservation.add(environmentalProtection.add(otherCost))))))));
                result = result.add(singlePmr);
            }
        }
        return result;
    }

    // 计算总到位资金 （中央、省、地方自筹）
    public static BigDecimal calOverallAvailableInvestment(List<ProjectMonthlyReportVO> projectMonthlyReportVOs) {

        BigDecimal result = new BigDecimal(0);

        if (projectMonthlyReportVOs.size() == 0) {
            return result;
        } else {
            for (ProjectMonthlyReportVO pmrVO : projectMonthlyReportVOs) {

                BigDecimal availableCentralInvestment = pmrVO.getAvailableCentralInvestment();
                BigDecimal availableLocalInvestment = pmrVO.getAvailableLocalInvestment();
                BigDecimal availableProvincialInvestment = pmrVO.getAvailableProvincialInvestment();

                BigDecimal singlePmr = availableCentralInvestment.add(availableLocalInvestment.
                        add(availableProvincialInvestment));
                result = result.add(singlePmr);
            }
        }
        return result;
    }



}
