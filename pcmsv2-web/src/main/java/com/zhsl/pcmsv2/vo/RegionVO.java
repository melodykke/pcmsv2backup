package com.zhsl.pcmsv2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionVO {

    private Integer regionId;

    private String regionCode;

    private String regionName;

    private Integer parentId;
}