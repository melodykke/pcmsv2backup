package com.zhsl.pcmsv2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.convertor.tovo.Announcement2VO;
import com.zhsl.pcmsv2.dto.AnnouncementDTO;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.mapper.AnnouncementMapper;
import com.zhsl.pcmsv2.model.Announcement;
import com.zhsl.pcmsv2.service.AnnouncementService;
import com.zhsl.pcmsv2.vo.AnnouncementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    /**
     * 根据ID查询单条公告
     * @param id
     * @return
     */
    @Override
    public AnnouncementVO findById(String id) {

        if (id == null || "".equals(id)) {
            log.error("【公告】 根据ID查询公告时，查询ID为空！");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        Announcement announcement = announcementMapper.selectByPrimaryKey(id);

        if (announcement == null) {
            log.error("【公告】 根据ID查询公告时，查询不到记录！");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }

        AnnouncementVO announcementVO = Announcement2VO.convert(announcement);

        return announcementVO;
    }

    /**
     * 查询置顶公告的分页信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<AnnouncementVO> findHotLatestsByPage(int pageNum, int pageSize) {
        List<Announcement> announcements = announcementMapper.findHotLatests();
        return buildPage(pageNum, pageSize, announcements);
    }

    /**
     * 查询所有公告的分页信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<AnnouncementVO> findByPage(int pageNum, int pageSize) {
        List<Announcement> announcements = announcementMapper.selectAll();
        return buildPage(pageNum, pageSize, announcements);
    }

    /**
     * 新增公告
     * @param announcementDTO
     * @return
     */
    @Override
    public int create(AnnouncementDTO announcementDTO) {

        if (announcementDTO == null) {
            log.error("【公告】 在新建公告时，公告信息为空！");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        String rtFileTempPath = announcementDTO.getRtFileTempPath();

        // 无文件上传的情况，直接存储公告
        if (rtFileTempPath == null || "".equals(rtFileTempPath)) {

        }

        return 0;
    }


    protected PageInfo<AnnouncementVO> buildPage(int pageNum, int pageSize, List<Announcement> contents) {
        if (contents == null) {
            log.error("【公告】 在查询公告时，公告信息为空！");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<AnnouncementVO> announcementVOs = contents.stream().map(e -> Announcement2VO.convert(e)).collect(Collectors.toList());
        PageInfo<AnnouncementVO> result = new PageInfo<>(announcementVOs);
        return result;
    }

}
