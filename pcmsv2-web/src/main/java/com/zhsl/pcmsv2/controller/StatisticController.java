package com.zhsl.pcmsv2.controller;

import com.zhsl.pcmsv2.browser.support.ResultVO;
import com.zhsl.pcmsv2.browser.util.ResultUtil;
import com.zhsl.pcmsv2.service.StatisticService;
import com.zhsl.pcmsv2.vo.RegionInvestmentVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/region/ri")
    @ApiOperation("管理员接口，获取区域的投资、资金到位等信息。" +
            "HTTP请求参数： regionId, startDate, endDate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "regionId", value = "区域ID，若空，则默认为此请求用户的管辖区域" +
                    "（也可以传自己管辖区域的子ID）", required = false, dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "统计开始时间，若空，则从2000年开始统计", required = false, dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "统计结束时间，若空，则为当前请求时间", required = false, dataType = "String"),
    })
    public ResultVO getRegionInvestment(HttpServletRequest request) {

        RegionInvestmentVO regionInvestmentVO = statisticService.calcRegionInvestmentStatistic(request);

        if (regionInvestmentVO != null) {
            return ResultUtil.success(regionInvestmentVO);
        }

        return ResultUtil.failed();
    }
}
