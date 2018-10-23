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

    // 检查用户是否具备某角色
    public static Boolean checkIfPossessARole(UserInfo thisUser, String role) {

        List<SysRole> sysRoles = thisUser.getRoles();
        if (sysRoles == null || sysRoles.size() == 0) {
            log.error("【角色判断】 角色判断时，用户角色为空");
            throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
        }

        List<String> roles = sysRoles.stream().map(e -> e.getRole()).collect(Collectors.toList());

        if (roles.contains(role)) {
           return true;
        }

        return false;

    }

}
