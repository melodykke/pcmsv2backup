package com.zhsl.pcmsv2.vo;

import com.zhsl.pcmsv2.model.PlantState;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BaseInfoVO {

    private String baseInfoId;

    private BigDecimal areaCoverage;

    private Integer branchProjectAmount;

    private String branchProjectOverview;

    private BigDecimal catchmentArea;

    private Integer cellProjectAmount;

    private String cellProjectOverview;

    private BigDecimal centralAccumulativePayment;

    private BigDecimal centralInvestment;

    private String constructionLand;

    private String county;

    private Date createTime;

    private String damType;

    private BigDecimal floodControlElevation;

    private String hasAcceptCompletion;

    private String hasProjectCompleted;

    private String hasSignedConstructionContract;

    private BigDecimal installedCapacity;

    private BigDecimal irrigatedArea;

    private String landReclamationPlan;

    private String latitude;

    private String legalPersonId;

    private String legalPersonName;

    private String legalRepresentativeId;

    private String legalRepresentativeName;

    private String level;

    private BigDecimal livestock;

    private BigDecimal localAccumulativePayment;

    private BigDecimal localInvestment;

    private String location;

    private String longitude;

    private BigDecimal maxDamHeight;

    private String overview;

    private String owner;

    private String parentId;

    private String plantName;

    private String projectSource;

    private String projectTask;

    private String projectType;

    private BigDecimal provincialAccumulativePayment;

    private BigDecimal provincialInvestment;

    private BigDecimal provincialLoan;

    private String remark;

    private BigDecimal ruralHumanWater;

    private String scale;

    private BigDecimal spillway;

    private BigDecimal storage;

    private String supervisorBid;

    private BigDecimal timeLimit;

    private BigDecimal totalInvestment;

    private Integer unitProjectAmount;

    private String unitProjectOverview;

    private Date updateTime;

    private BigDecimal utilizablCapacity;

    private BigDecimal waterSupplyPopulation;

    private BigDecimal watersupply;

    private Byte state;

    private Integer regionId;

    private String affiliatedTo;

    private PlantStateVO plantStateVO;

    private Date commenceDate;
}
