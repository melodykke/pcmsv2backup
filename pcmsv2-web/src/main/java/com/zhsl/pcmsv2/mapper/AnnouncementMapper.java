package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.Announcement;

import java.util.List;

public interface AnnouncementMapper {
    int deleteByPrimaryKey(String announcementId);

    int insert(Announcement record);

    Announcement selectByPrimaryKey(String announcementId);

    List<Announcement> selectAll();

    int updateByPrimaryKey(Announcement record);

    List<Announcement> findHotLatests();
}