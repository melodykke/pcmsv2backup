package com.zhsl.pcmsv2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlantStateVO {
    private Integer stateId;

    private String description;

    private String type;

    private Date updateTime;

    private Date createTime;
}