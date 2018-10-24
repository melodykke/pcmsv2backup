package com.zhsl.pcmsv2.service;

import com.github.pagehelper.PageInfo;
import com.zhsl.pcmsv2.model.Announcement;
import com.zhsl.pcmsv2.vo.AnnouncementVO;

import java.util.List;

public interface AnnouncementService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    AnnouncementVO findById(String id);

    /**
     * 查询置顶公告的分页信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<AnnouncementVO> findHotLatestsByPage(int pageNum, int pageSize);

    /**
     * 查询所有公告的分页信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<AnnouncementVO> findByPage(int pageNum, int pageSize);
}
