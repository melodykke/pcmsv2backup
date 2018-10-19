package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper {

    /**
     * 增添用户
     * @param record
     * @return
     */
    int insert(UserInfo userInfo);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    int deleteByPrimaryKey(String userId);

    /**
     * 修改用户
     * @param record
     * @return
     */
    int updateByPrimaryKey(UserInfo record);

    /**
     * 根据主键查找
     * @param userId
     * @return
     */
    UserInfo selectByPrimaryKey(String userId);

    /**
     * 查找全部用户
     * @return
     */
    List<UserInfo> selectAll();

    /**
     * 根据用户名查找
     * @param username
     * @return
     */
    UserInfo findByUsername(String username);

    /**
     * 通过 用户名 或者 ID 查找用户及用户的角色信息
     * @param username
     * @param userId
     * @return
     */
    UserInfo findOneWithRolesByUsernameOrId(@Param("username") String username, @Param("userId") String userId);

    /**
     * 通过 用户名 或者ID 查找用户、用户的角色及权限信息
     * @param username
     * @param userId
     * @return
     */
    UserInfo findOneWithRolesAndPrivilegesByUsernameOrId(@Param("username") String username, @Param("userId") String userId);

    /**
     * 根据parentId查找父账号
     * @param parentId
     * @return
     */
    UserInfo findParentByParentId(String parentId);

    /**
     * 根据userId找到当前账号的子账号
     * @param userId
     * @return
     */
    List<UserInfo> findChildrenByUserId(String userId);

    /**
     * 更新用户集合里用户的baseInfoId值
     * @param userInfos
     * @param baseInfoId
     * @return
     */
    Integer batchUpdateBaseInfoId(@Param("userInfos") List<UserInfo> userInfos, @Param("baseInfoId") String baseInfoId);

    /**
     * 查找当前账号的下一级账号集合
     * @return
     */
    List<UserInfo> findAllInferior(@Param("userId") String userId);

    /**
     * 列出所有用户名 主要是防止注册用户名重复用
     * @return
     */
    List<String> findAllUsername();

    int changeBaseInfoIdToUUID(@Param("baseInfoId") String baseInfoId, @Param("userId") String userId);

}