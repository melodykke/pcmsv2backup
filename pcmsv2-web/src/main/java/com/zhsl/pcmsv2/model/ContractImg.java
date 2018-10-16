package com.zhsl.pcmsv2.model;

import lombok.Data;

import java.util.Date;

@Data
public class ContractImg {
    private String contractImgId;

    private Date createTime;

    private String imgAddr;

    private String imgDesc;

    private String contractId;
}