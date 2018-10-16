package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(String roleId);

    int insert(SysRole record);

    SysRole selectByPrimaryKey(String roleId);

    List<SysRole> selectAll();

    int updateByPrimaryKey(SysRole record);

    SysRole findByRole(@Param("role") String role);

}