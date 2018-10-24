package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.dto.UsersRoles;
import com.zhsl.pcmsv2.model.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper {

    /**
     * 新增记录
     * @param record
     * @return
     */
    int insert(SysUserRole record);

    /**
     * 查找全部
     * @return
     */
    List<SysUserRole> selectAll();

    /**
     * 批量新增用户和角色的关系
     * @param usersRoles
     * @return
     */
    int batchInsert(UsersRoles usersRoles);

    /**
     * 根据userId批量查找
     * @param UserId
     * @return
     */
    List<SysUserRole> selectByUserId(String UserId);

    /**
     * 根据userId批量删除记录
     * @param UserId
     * @return
     */
    int deleteByUserId(String UserId);
}