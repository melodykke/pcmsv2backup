package com.zhsl.pcmsv2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhsl.pcmsv2.model.SysPermission;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleVO implements GrantedAuthority {
    private String roleId;

    private Boolean available;

    private String description;

    private String role;

    private List<SysPermission> permissions;

    @Override
    public String getAuthority() {
        return role;
    }
}