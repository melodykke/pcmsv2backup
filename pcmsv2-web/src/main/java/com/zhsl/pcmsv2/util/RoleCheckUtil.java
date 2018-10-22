package com.zhsl.pcmsv2.util;

import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.model.Region;
import com.zhsl.pcmsv2.model.SysRole;
import com.zhsl.pcmsv2.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoleCheckUtil {

    private static Logger log = LoggerFactory.getLogger("RoleCheckUtil.class");

    public static Boolean checkIfPossessProvinceRole(UserInfo thisUser) {

        List<SysRole> sysRoles = thisUser.getRoles();
        if (sysRoles == null || sysRoles.size() == 0) {
            log.error("【月报】 按区域查询月报时，用户角色为空");
            throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
        }

        List<String> roles = sysRoles.stream().map(e -> e.getRole()).collect(Collectors.toList());

        if (roles.contains("ROLE_PROVINCE")) {
           return true;
        }

        return false;

    }

}
