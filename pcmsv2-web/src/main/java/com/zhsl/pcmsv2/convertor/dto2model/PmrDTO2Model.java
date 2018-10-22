package com.zhsl.pcmsv2.convertor.dto2model;

import com.zhsl.pcmsv2.dto.ProjectMonthlyReportDTO;
import com.zhsl.pcmsv2.model.ProjectMonthlyReport;
import com.zhsl.pcmsv2.util.BeanUtils;

public class PmrDTO2Model {

    public static ProjectMonthlyReport convert(ProjectMonthlyReportDTO projectMonthlyReportDTO) {
        ProjectMonthlyReport projectMonthlyReport = new ProjectMonthlyReport();
        BeanUtils.copyProperties(projectMonthlyReportDTO, projectMonthlyReport);
        return projectMonthlyReport;
    }

}
