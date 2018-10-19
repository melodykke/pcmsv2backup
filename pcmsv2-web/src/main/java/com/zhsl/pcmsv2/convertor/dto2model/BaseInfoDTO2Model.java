package com.zhsl.pcmsv2.convertor.dto2model;

import com.zhsl.pcmsv2.dto.BaseInfoDTO;
import com.zhsl.pcmsv2.model.BaseInfo;
import org.springframework.beans.BeanUtils;

public class BaseInfoDTO2Model {

    public static BaseInfo convert(BaseInfoDTO baseInfoDTO) {
        BaseInfo baseInfo = new BaseInfo();
        BeanUtils.copyProperties(baseInfoDTO, baseInfo);
        return baseInfo;
    }

}
