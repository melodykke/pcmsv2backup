package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper {
    int insert(SysUserRole record);

    List<SysUserRole> selectAll();
}