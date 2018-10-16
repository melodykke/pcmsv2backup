package com.zhsl.pcmsv2.model;

import java.util.Date;

public class BaseInfoImg {
    private String baseInfoImgId;

    private Date createTime;

    private String imgAddr;

    private String imgDesc;

    private String baseInfoId;

    public String getBaseInfoImgId() {
        return baseInfoImgId;
    }

    public void setBaseInfoImgId(String baseInfoImgId) {
        this.baseInfoImgId = baseInfoImgId == null ? null : baseInfoImgId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getImgAddr() {
        return imgAddr;
    }

    public void setImgAddr(String imgAddr) {
        this.imgAddr = imgAddr == null ? null : imgAddr.trim();
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc == null ? null : imgDesc.trim();
    }

    public String getBaseInfoId() {
        return baseInfoId;
    }

    public void setBaseInfoId(String baseInfoId) {
        this.baseInfoId = baseInfoId == null ? null : baseInfoId.trim();
    }
}