package com.zhsl.pcmsv2.convertor.tovo;


import com.zhsl.pcmsv2.model.BaseInfo;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.vo.BaseInfoVO;

public class BaseInfo2VO {

    public static BaseInfoVO convert(BaseInfo baseInfo) {
        BaseInfoVO baseInfoVO = new BaseInfoVO();
        BeanUtils.copyProperties(baseInfo, baseInfoVO);
        return baseInfoVO;
    }

}
