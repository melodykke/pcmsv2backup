package com.zhsl.pcmsv2.controller;

import com.github.pagehelper.PageInfo;
import com.zhsl.pcmsv2.browser.support.ResultVO;
import com.zhsl.pcmsv2.browser.util.ResultUtil;
import com.zhsl.pcmsv2.service.AnnouncementService;
import com.zhsl.pcmsv2.vo.AnnouncementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/announcement")
@RestController
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    public ResultVO getById(@PathVariable String id) {


        AnnouncementVO announcementVO = announcementService.findById(id);

        if (announcementVO != null) {
            return ResultUtil.success(announcementVO);
        } else {
            return ResultUtil.failed();
        }
    }

    @GetMapping("/hot")
    public ResultVO getHotAnnouncement(@RequestParam(required = false, name = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(required = false, name = "pageSize", defaultValue = "15") Integer pageSize) {

        PageInfo<AnnouncementVO> pageInfo = announcementService.findHotLatestsByPage(pageNum, pageSize);

        return ResultUtil.success(pageInfo);
    }


}