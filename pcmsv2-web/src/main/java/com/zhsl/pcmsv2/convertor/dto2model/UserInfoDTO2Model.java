package com.zhsl.pcmsv2.convertor.dto2model;

import com.zhsl.pcmsv2.dto.UserInfoDTO;
import com.zhsl.pcmsv2.model.UserInfo;

import org.springframework.beans.BeanUtils;

public class UserInfoDTO2Model {

    public static UserInfo convert(UserInfoDTO userInfoDTO) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoDTO, userInfo);
        return userInfo;
    }

}
