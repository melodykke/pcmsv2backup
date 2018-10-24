package com.zhsl.pcmsv2.convertor.tovo;

import com.zhsl.pcmsv2.model.SysRole;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.vo.SysRoleVO;

public class SysRole2VO {
    public static SysRoleVO convert(SysRole sysRole) {
        SysRoleVO sysRoleVO = new SysRoleVO();
        BeanUtils.copyProperties(sysRole, sysRoleVO);
        return sysRoleVO;
    }
}
