package com.zhsl.pcmsv2.vo;

import com.zhsl.pcmsv2.model.ProjectMonthlyReportImg;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProjectMonthlyReportVO {

    private String projectMonthlyReportId;

    private BigDecimal availableCentralInvestment;

    private BigDecimal availableLocalInvestment;

    private BigDecimal availableProvincialInvestment;

    private BigDecimal backfill;

    private BigDecimal civilEngineering;

    private BigDecimal concrete;

    private String constructionContent;

    private Date createTime;

    private Date updateTime;

    private String difficulty;

    private BigDecimal electromechanicalEquipment;

    private BigDecimal environmentalProtection;

    private BigDecimal grout;

    private BigDecimal holeDug;

    private BigDecimal independentCost;

    private BigDecimal labourForce;

    private BigDecimal masonry;

    private String measure;

    private BigDecimal metalMechanism;

    private BigDecimal openDug;

    private BigDecimal otherCost;

    private BigDecimal rebar;

    private String remark;

    private BigDecimal resettlementArrangement;

    private BigDecimal sourceCentralInvestment;

    private BigDecimal sourceLocalInvestment;

    private BigDecimal sourceProvincialInvestment;

    private Date submitDate;

    private String submitter;

    private String suggestion;

    private BigDecimal temporaryWork;

    private String visualProgress;

    private BigDecimal waterConservation;

    private String baseInfoId;

    private String statisticalLeader;

    private List<ProjectMonthlyReportImg> projectMonthlyReportImgs;
}
