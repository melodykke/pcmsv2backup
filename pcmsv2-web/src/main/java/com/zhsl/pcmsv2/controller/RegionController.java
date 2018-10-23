package com.zhsl.pcmsv2.controller;

import com.zhsl.pcmsv2.browser.support.ResultVO;
import com.zhsl.pcmsv2.browser.util.ResultUtil;
import com.zhsl.pcmsv2.model.Region;
import com.zhsl.pcmsv2.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/region")
@RestController
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping("/all")
    public ResultVO getAll() {

        List<Region> regions = regionService.findAll();

        return ResultUtil.success(regions);
    }

}
