package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.AnnouncementFile;

import java.util.List;

public interface AnnouncementFileMapper {
    int deleteByPrimaryKey(String announcementFileId);

    int insert(AnnouncementFile record);

    AnnouncementFile selectByPrimaryKey(String announcementFileId);

    List<AnnouncementFile> selectAll();

    int updateByPrimaryKey(AnnouncementFile record);
}