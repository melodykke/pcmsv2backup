package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.Announcement;

import java.util.List;

public interface AnnouncementMapper {
    /**
     * 根据ID删除记录
     * @param announcementId
     * @return
     */
    int deleteByPrimaryKey(String announcementId);

    /**
     * 新增记录
     * @param record
     * @return
     */
    int insert(Announcement record);

    /**
     * 根据ID查找记录
     * @param announcementId
     * @return
     */
    Announcement selectByPrimaryKey(String announcementId);

    /**
     * 查找全部记录
     * @return
     */
    List<Announcement> selectAll();

    /**
     * 更新记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(Announcement record);

    /**
     * 查找所有置顶的公告
     * @return
     */
    List<Announcement> findHotLatests();
}