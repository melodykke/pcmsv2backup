package com.zhsl.pcmsv2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionInvestmentVO {

    private String regionName;

    private Integer regionId;

    private String regionCode;

    private BigDecimal totalInvestment;

    private BigDecimal investmentSofar;

    private BigDecimal availableInvestmentSofar;

    private List<RegionInvestmentVO> children;
}
