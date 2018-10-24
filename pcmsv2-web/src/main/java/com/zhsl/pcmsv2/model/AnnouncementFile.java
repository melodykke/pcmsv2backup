package com.zhsl.pcmsv2.model;

import java.util.Date;

public class AnnouncementFile {
    private String announcementFileId;

    private Date createTime;

    private String fileAddr;

    private String fileDesc;

    private String announcementId;

    public String getAnnouncementFileId() {
        return announcementFileId;
    }

    public void setAnnouncementFileId(String announcementFileId) {
        this.announcementFileId = announcementFileId == null ? null : announcementFileId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFileAddr() {
        return fileAddr;
    }

    public void setFileAddr(String fileAddr) {
        this.fileAddr = fileAddr == null ? null : fileAddr.trim();
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc == null ? null : fileDesc.trim();
    }

    public String getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(String announcementId) {
        this.announcementId = announcementId == null ? null : announcementId.trim();
    }
}