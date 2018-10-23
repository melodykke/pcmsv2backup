package com.zhsl.pcmsv2.convertor.tovo;

import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.vo.RegionVO;
import com.zhsl.pcmsv2.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;

public class UserInfo2VO {

    public static UserInfoVO convert(UserInfo userInfo) {
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);
        RegionVO regionVO = new RegionVO();
        if (userInfo.getRegion() != null) {
            BeanUtils.copyProperties(userInfo.getRegion(), regionVO);
            userInfoVO.setRegionVO(regionVO);
        }
        return userInfoVO;
    }

}
