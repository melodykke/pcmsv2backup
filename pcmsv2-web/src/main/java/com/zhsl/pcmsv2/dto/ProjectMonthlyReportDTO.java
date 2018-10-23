package com.zhsl.pcmsv2.dto;

import com.zhsl.pcmsv2.model.ProjectMonthlyReportImg;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProjectMonthlyReportDTO {

    private String projectMonthlyReportId;

    private BigDecimal availableCentralInvestment;

    private BigDecimal availableLocalInvestment;

    private BigDecimal availableProvincialInvestment;

    private BigDecimal backfill;

    private BigDecimal civilEngineering;

    private BigDecimal concrete;

    private String constructionContent;

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

    @Null
    @Past(message = "必须是一个过去的时间")
    private Date submitDate;

    @NotBlank(message = "月报提交人员姓名不能为空")
    private String submitter;

    private String suggestion;

    private BigDecimal temporaryWork;

    private String visualProgress;

    private BigDecimal waterConservation;

    @NotBlank(message = "月报数据统计人员姓名不能为空")
    private String statisticalLeader;

    private List<ProjectMonthlyReportImg> projectMonthlyReportImgs;
}
