package com.zhsl.pcmsv2.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class BaseInfoDTO {

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

    private String owner; // 法人

    @NotBlank(message = "水库项目名不能为空")
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

    private BigDecimal utilizablCapacity;

    private BigDecimal waterSupplyPopulation;

    private BigDecimal watersupply;

    @NotNull(message = "工程项目所属区县不能为空")
    private Integer regionId;

    @NotBlank(message = "工程项目直管单位不能为空")
    private String affiliatedTo;
}