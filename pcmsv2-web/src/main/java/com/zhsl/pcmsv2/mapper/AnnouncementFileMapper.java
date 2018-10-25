package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.AnnouncementFile;

import java.util.List;

public interface AnnouncementFileMapper {

    /**
     * 根据主键删除记录
     * @param announcementFileId
     * @return
     */
    int deleteByPrimaryKey(String announcementFileId);

    /**
     * 单条插入
     * @param record
     * @return
     */
    int insert(AnnouncementFile record);

    /**
     * 根据主键查询记录
     * @param announcementFileId
     * @return
     */
    AnnouncementFile selectByPrimaryKey(String announcementFileId);

    /**
     * 查找全部记录
     * @return
     */
    List<AnnouncementFile> selectAll();

    /**
     * 更新记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(AnnouncementFile record);

    /**
     * 批量存储文件信息
     * @param announcementFiles
     * @return
     */
    int batchInsert(List<AnnouncementFile> announcementFiles);
}