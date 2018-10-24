package com.zhsl.pcmsv2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnouncementVO {

    private String announcementId;

    private Date createTime;

    private Boolean hot;

    private String keyword;

    private String title;

    private String type;

    private Date updateTime;

    private String content;
}
