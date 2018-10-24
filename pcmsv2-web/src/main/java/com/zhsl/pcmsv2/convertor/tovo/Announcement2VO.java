package com.zhsl.pcmsv2.convertor.tovo;


import com.zhsl.pcmsv2.model.Announcement;
import com.zhsl.pcmsv2.model.BaseInfo;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.vo.AnnouncementVO;
import com.zhsl.pcmsv2.vo.BaseInfoVO;
import com.zhsl.pcmsv2.vo.PlantStateVO;

public class Announcement2VO {

    public static AnnouncementVO convert(Announcement announcement) {
        AnnouncementVO announcementVO = new AnnouncementVO();
        BeanUtils.copyProperties(announcement, announcementVO);
        return announcementVO;
    }

}
