package com.zhsl.pcmsv2.model;


import java.util.Date;
import java.util.List;

public class Tender {

    private String tenderId;
    private String tenderFilingUnit; // 招标备案单位
    private String nameOfLots; // 标段名称
    private Date bidPlanDate;
    private Date bidCompleteDate;
    private String bidAgent;
    private String tenderAgent;

    private BaseInfo baseInfo;
    private Byte state;

    private List<TenderImg> tenderImgs;
    private String submitter;
    private Date createTime;
    private Date updateTime;
}
