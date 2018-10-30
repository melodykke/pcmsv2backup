package com.zhsl.pcmsv2.controller;

import com.zhsl.pcmsv2.browser.support.ResultVO;
import com.zhsl.pcmsv2.browser.util.ResultUtil;
import com.zhsl.pcmsv2.dto.ProjectMonthlyReportDTO;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.service.MonthReportService;
import com.zhsl.pcmsv2.vo.ProjectMonthlyReportVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pmr")
public class PmrController {

    @Autowired
    private MonthReportService monthReportService;

    @GetMapping("/management/all")
    public ResultVO getAll() {

        List<ProjectMonthlyReportVO> projectMonthlyReportVOs = monthReportService.findAll();

        return ResultUtil.success(projectMonthlyReportVOs);
    }

    @PostMapping
    public ResultVO create(@Valid @RequestBody ProjectMonthlyReportDTO projectMonthlyReportDTO) {

        int result = monthReportService.create(projectMonthlyReportDTO);

        if (result == 1) {
            monthReportService.syncToRedis();
            return ResultUtil.success();
        }

        return ResultUtil.failed();
    }

    @DeleteMapping("/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    @ApiOperation(value = "普通用户调用的删除方法，只能删除自己的月报")
    public ResultVO deleteById(@PathVariable String id) {

        int result = monthReportService.delete(id);

        if (result == 1) {
            return ResultUtil.success();
        } else {
            return ResultUtil.failed();
        }
    }

    @DeleteMapping("/management/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    public ResultVO managementDeleteById(@PathVariable String id) {

        int result = monthReportService.managementDelete(id);

        if (result == 1) {
            return ResultUtil.success();
        } else {
            return ResultUtil.failed();
        }
    }

    @PutMapping("/management/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    public ResultVO managementModify(@PathVariable String id, ProjectMonthlyReportDTO projectMonthlyReportDTO) {

        projectMonthlyReportDTO.setProjectMonthlyReportId(id);

        int result = monthReportService.managementModify(projectMonthlyReportDTO);

        if (result == 1) {
            monthReportService.syncToRedis();
            return ResultUtil.success();
        }

        return ResultUtil.failed();
    }

    @GetMapping("/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    @ApiOperation(value = "普通用户调用的查询方法，只能查询自己的月报")
    public ResultVO findById(@PathVariable String id) {

        ProjectMonthlyReportVO projectMonthlyReportVO = monthReportService.findById(id);

        return ResultUtil.success(projectMonthlyReportVO);
    }

    @GetMapping("/management/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    public ResultVO managementFindById(@PathVariable String id) {

        ProjectMonthlyReportVO projectMonthlyReportVO = monthReportService.managementFindById(id);

        return ResultUtil.success(projectMonthlyReportVO);
    }

    @GetMapping("/period")
    @ApiOperation(value = "普通用户调用的时间区间查询方法，只能查询自己的月报")
    public ResultVO findByBaseInfoIdAndPeriodWithImg(@AuthenticationPrincipal UserInfo userInfo, HttpServletRequest request) {
        String startDate = null;
        String endDate = null;
        try {
            startDate = ServletRequestUtils.getStringParameter(request, "startDate");
            endDate = ServletRequestUtils.getStringParameter(request, "endDate");
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }

        List<ProjectMonthlyReportVO> projectMonthlyReportVOs = monthReportService
                .findByBaseInfoIdAndPeriodWithImg(userInfo.getBaseInfoId(), startDate, endDate);

        return ResultUtil.success(projectMonthlyReportVOs);
    }

    @GetMapping
    public ResultVO findByBaseInfoIdWithImg() {

        List<ProjectMonthlyReportVO> projectMonthlyReportVOs = monthReportService.findByBaseInfoIdWithImg();

        return ResultUtil.success(projectMonthlyReportVOs);
    }

    /**
     * 按登陆用户所在区域获取所有水库月报的投资完成情况总和
     */
    @GetMapping("/overallinvestmentcompletion")
    @ApiOperation(value = "按登陆用户所在区域获取所有水库月报的投资完成情况总和(management)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "by", value = "如果根据地区查询：region; 如果根据水库查询：baseInfo", required = false, dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "月报开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "月报结束时间", required = false, dataType = "String"),
            @ApiImplicitParam(name = "regionId", value = "需要计算的区域ID，此项只能最高级用户才能传参", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "baseInfoId", value = "计算某一个水库的投资完成总额，此项只能最高级用户才能传参(此项和其他参数互斥)", required = false, dataType = "String")
    })
    public ResultVO getOverallInvestmentCompletion(@RequestParam String by, HttpServletRequest request) {
        String startDate = "";
        String endDate = "";
        int regionId = 0;
        String baseInfoId = "";
        try {
            startDate = ServletRequestUtils.getStringParameter(request,"startDate");
            endDate = ServletRequestUtils.getStringParameter(request, "endDate");
            regionId = ServletRequestUtils.getIntParameter(request, "regionId", 0);
            baseInfoId = ServletRequestUtils.getStringParameter(request, "baseInfoId");
        } catch (ServletRequestBindingException e) {
            log.error("【月报】 计算用户区域内所有工程总投资完成时， 用户端传来的startDate或endDate参数接收出错，程序将采用默认值");
        }

        BigDecimal result = BigDecimal.ZERO;

        result = monthReportService.calcOverallInvestmentCompletion(baseInfoId, regionId, startDate, endDate, by);

        return ResultUtil.success(result);
    }


}
