package com.zhsl.pcmsv2.service;

import com.zhsl.pcmsv2.dto.UserInfoDTO;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.vo.UserInfoVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * 查询用户 根据用户ID查询 只是用户的信息
     * @param id
     * @return
     */
    UserInfoVO findById(String id);

    /**
     * 查询用户 根据用户username查询
     * @param id
     * @return
     */
    UserInfoVO findByUsername(String username);

    /**
     * 创建用户
     * @param userInfoDTO
     * @return
     */
    int create(UserInfoDTO userInfoDTO);

    /**
     * 删除用户
     * @param userInfoDTO
     * @return
     */
    int delete(String userId);

    /**
     * 修改用户
     * @param userInfoDTO
     * @return
     */
    int modify(UserInfoDTO userInfoDTO);

    /**
     * 通过用户名或ID查找用户包括用户的角色和权限信息
     * @param usernameOrId
     * @return
     */
    UserInfoVO findUserInfoByUsernameOrIdWithRolesAndPrivileges(String usernameOrId);

    /**
     * 查询所有的用户 （不包含最上级用户）
     * @return
     */
    List<UserInfoVO> findAll();

    /**
     * 将数据库中userinfo信息同步至redis
     */
    void syncToRedis();

}
