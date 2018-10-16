package com.zhsl.pcmsv2.model;

import lombok.Data;

import java.util.Date;

@Data
public class Notification {
    private String notificationId;

    private String baseInfoId;

    private Boolean checked;

    private Date createTime;

    private String submitter;

    private String type;

    private String typeId;

    private String url;

    private String yearmonth;
}