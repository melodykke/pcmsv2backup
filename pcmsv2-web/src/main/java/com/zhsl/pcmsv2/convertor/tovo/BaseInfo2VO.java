package com.zhsl.pcmsv2.convertor.tovo;


import com.zhsl.pcmsv2.model.BaseInfo;
import com.zhsl.pcmsv2.model.PlantState;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.vo.BaseInfoVO;
import com.zhsl.pcmsv2.vo.PlantStateVO;

public class BaseInfo2VO {

    public static BaseInfoVO convert(BaseInfo baseInfo) {
        BaseInfoVO baseInfoVO = new BaseInfoVO();
        BeanUtils.copyProperties(baseInfo, baseInfoVO);
        PlantStateVO plantStateVO = new PlantStateVO();
        if (baseInfo.getPlantState() != null) {
            BeanUtils.copyProperties(baseInfo.getPlantState(), plantStateVO);
            baseInfoVO.setPlantStateVO(plantStateVO);
        }
        return baseInfoVO;
    }

}
