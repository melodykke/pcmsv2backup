package com.zhsl.pcmsv2.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LifeCircle {

    // 建设总时间
    private BigDecimal timeLimit;

    // 建设持续时间（到目前为止）
    private BigDecimal timeSofar;

    // 工程总投资
    private BigDecimal totalInvestment;

    // 到目前为止的投资完成
    private BigDecimal investmentSofar;

    // 到目前为止的资金到位
    private BigDecimal availableInvestmentSofar;

    public LifeCircle(BigDecimal timeLimit, BigDecimal timeSofar, BigDecimal totalInvestment, BigDecimal investmentSofar, BigDecimal availableInvestmentSofar) {
        this.timeLimit = timeLimit;
        this.timeSofar = timeSofar;
        this.totalInvestment = totalInvestment;
        this.investmentSofar = investmentSofar;
        this.availableInvestmentSofar = availableInvestmentSofar;
    }
}
