package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.SysRolePermission;

import java.util.List;

public interface SysRolePermissionMapper {
    int insert(SysRolePermission record);

    List<SysRolePermission> selectAll();
}