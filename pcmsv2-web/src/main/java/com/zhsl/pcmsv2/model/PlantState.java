package com.zhsl.pcmsv2.model;

import java.util.Date;

public class PlantState {
    private Integer stateId;

    private String description;

    private String type;

    private Date updateTime;

    private Date createTime;


    public PlantState() {
    }

    public PlantState(Integer stateId) {
        this.stateId = stateId;
    }


    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}