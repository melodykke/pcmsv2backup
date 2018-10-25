package com.zhsl.pcmsv2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhsl.pcmsv2.model.AnnouncementFile;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

    private List<AnnouncementFile> files;
}
