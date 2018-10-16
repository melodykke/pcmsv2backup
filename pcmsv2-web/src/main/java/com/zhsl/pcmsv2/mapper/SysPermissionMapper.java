package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.SysPermission;

import java.util.List;

public interface SysPermissionMapper {
    int deleteByPrimaryKey(String permissionId);

    int insert(SysPermission record);

    SysPermission selectByPrimaryKey(String permissionId);

    List<SysPermission> selectAll();

    int updateByPrimaryKey(SysPermission record);
}