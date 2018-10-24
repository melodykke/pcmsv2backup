package com.zhsl.pcmsv2.dto;

import com.zhsl.pcmsv2.vo.SysRoleVO;
import com.zhsl.pcmsv2.vo.UserInfoVO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UsersRoles {

    @NotNull(message = "必须选择至少一个要配置权限的用户！")
    @Size(min=1)
    List<String> userIds;

    @NotNull(message = "必须选择一个要配置的权限！")
    @Size(max=1)
    List<String> roleIds;

}
