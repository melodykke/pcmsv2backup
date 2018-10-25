package com.zhsl.pcmsv2.convertor.dto2model;


import com.zhsl.pcmsv2.dto.AnnouncementDTO;
import com.zhsl.pcmsv2.model.Announcement;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.vo.AnnouncementVO;

public class AnnouncementDTO2Model {

    public static Announcement convert(AnnouncementDTO announcementDTO) {
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(announcementDTO, announcement);
        return announcement;
    }

}
