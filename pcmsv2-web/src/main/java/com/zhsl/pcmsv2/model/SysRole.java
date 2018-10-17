package com.zhsl.pcmsv2.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
public class SysRole implements GrantedAuthority {
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