package com.zhsl.pcmsv2.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;


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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "roleId='" + roleId + '\'' +
                ", available=" + available +
                ", description='" + description + '\'' +
                ", role='" + role + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}