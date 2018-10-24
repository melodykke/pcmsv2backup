package com.zhsl.pcmsv2.convertor.tovo;

import com.zhsl.pcmsv2.model.SysRole;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.vo.RegionVO;
import com.zhsl.pcmsv2.vo.SysRoleVO;
import com.zhsl.pcmsv2.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserInfo2VO {

    public static UserInfoVO convert(UserInfo userInfo) {
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);

        RegionVO regionVO = new RegionVO();
        if (userInfo.getRegion() != null) {
            BeanUtils.copyProperties(userInfo.getRegion(), regionVO);
            userInfoVO.setRegionVO(regionVO);
        }

        if (userInfo.getRoles() != null && userInfo.getRoles().size() > 0) {
            List<SysRole> sysRoles = userInfo.getRoles();
            List<SysRoleVO> sysRoleVOs = sysRoles.stream().map(e -> SysRole2VO.convert(e)).collect(Collectors.toList());
            userInfoVO.setRoleVOs(sysRoleVOs);
        }
        return userInfoVO;
    }

}
