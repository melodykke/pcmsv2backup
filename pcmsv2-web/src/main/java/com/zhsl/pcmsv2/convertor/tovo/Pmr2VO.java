package com.zhsl.pcmsv2.convertor.tovo;

import com.zhsl.pcmsv2.model.ProjectMonthlyReport;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.vo.ProjectMonthlyReportVO;

public class Pmr2VO {

    public static ProjectMonthlyReportVO convert(ProjectMonthlyReport projectMonthlyReport) {
        ProjectMonthlyReportVO projectMonthlyReportVO = new ProjectMonthlyReportVO();
        BeanUtils.copyProperties(projectMonthlyReport, projectMonthlyReportVO);
        return projectMonthlyReportVO;
    }

}
