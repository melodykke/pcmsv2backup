package com.zhsl.pcmsv2.service.impl;

import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.convertor.tovo.UserInfo2VO;
import com.zhsl.pcmsv2.dto.UserInfoDTO;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.mapper.UserInfoMapper;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.service.UserService;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.util.UUIDUtils;
import com.zhsl.pcmsv2.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.zhsl.pcmsv2.convertor.dto2model.UserInfoDTO2Model.convert;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("用户名为：" + username + "的用户正在尝试登陆...");

        // 根据用户名查找用户信息
        UserInfo userInfo = userInfoMapper.findOneWithRolesAndPrivilegesByUsernameOrId(username, null);

        if (userInfo == null) {
            throw new UsernameNotFoundException("不存在此用户！");
        }

        return userInfo;
    }

    @Override
    public UserInfoVO findById(String id) {

        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);

        UserInfoVO userInfoVO = UserInfo2VO.convert(userInfo);

        return userInfoVO;
    }

    @Override
    public UserInfoVO findByUsername(String username) {
        UserInfo userInfo = userInfoMapper.findByUsername(username);

        UserInfoVO userInfoVO = UserInfo2VO.convert(userInfo);

        return userInfoVO;
    }

    @Override
    public int create(UserInfoDTO userInfoDTO) {

        if (userInfoDTO == null) {
            log.error("【用户】 创建用户时，收到的用户信息UserInfoDTO为空");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        if (!userInfoDTO.getPassword().equals(userInfoDTO.getRePassword())) {
            log.error("【用户】 创建用户时，密码和确认密码不一致");
            throw new SysException(SysEnum.INCONSISTENT_PASSWORD_ERROR);
        }

        List<String> allUsername = userInfoMapper.findAllUsername();

        if (allUsername.contains(userInfoDTO.getUsername())) {
            log.error("【用户】 创建用户时，已存在拟申请用户名");
            throw new SysException(SysEnum.USER_INFO_DUPLICATED);
        }

        UserInfo userInfo = convert(userInfoDTO);

        userInfo.setUserId(UUIDUtils.getUUID().toUpperCase());
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setPassword(passwordEncoder.encode(userInfoDTO.getPassword()));

        int result = userInfoMapper.insert(userInfo);

        return result;
    }

    @Override
    public int delete(String userId) {
        int result = userInfoMapper.deleteByPrimaryKey(userId);
        return result;
    }

    /**
     * 修改用户信息， 用户名和用户ID是不可变的，其他信息封装到UserInfoDTO里，对于不为null的信息才修改。
     * 密码必须符合规范才能修改
     * @param userInfoDTO
     * @return
     */
    @Override
    public int modify(UserInfoDTO userInfoDTO) {

        if (userInfoDTO == null) {
            log.error("【用户】 没有收到userInfoDTO , " +
                    "实际userInfoDTO = {}", userInfoDTO);
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        if (userInfoDTO.getUserId() == null || "".equals(userInfoDTO.getUserId())) {
            log.error("【用户】 修改用户时，收到的用户ID为空 userInfoDTO：{}", userInfoDTO);
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        // 从数据库取出要修改的用户信息记录  UserInfo  ID和username是不可变的
        UserInfo target = userInfoMapper.selectByPrimaryKey(userInfoDTO.getUserId());

        // 确认该信息存在
        if (target == null) {
            log.error("【用户】 修改用户时，拟修改的用户不存在");
            throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }
        String targetId = target.getUserId();
        String targetUsername = target.getUsername();
        String targetPassword = target.getPassword();

        // 将修改信息注入target
        BeanUtils.copyProperties(userInfoDTO, target);

        // 是否需要处理密码
        if (userInfoDTO.getPassword() != null && userInfoDTO.getRePassword() != null) {

            if (!userInfoDTO.getPassword().equals(userInfoDTO.getRePassword())) {
                log.error("【用户】 创建用户时，密码和确认密码不一致");
                throw new SysException(SysEnum.INCONSISTENT_PASSWORD_ERROR);
            }

            if (userInfoDTO.getPassword().length() > 12 || userInfoDTO.getPassword().length() < 4 ||
                    userInfoDTO.getRePassword().length() > 12 || userInfoDTO.getRePassword().length() < 4) {
                log.error("【用户】 创建用户时，密码的位数不符合要求 （4<长度<12）");
                throw new SysException(SysEnum.INVALID_PASSWORD_FORMAT);
            }

            target.setPassword(passwordEncoder.encode(userInfoDTO.getPassword()));

        } else if (userInfoDTO.getPassword() == null && userInfoDTO.getRePassword() == null) {
            target.setPassword(targetPassword);
        } else {
            log.error("【用户】 修改用户时，用户输入的密码或确认密码为空");
            throw new SysException(SysEnum.INCONSISTENT_PASSWORD_ERROR);
        }

        target.setUserId(targetId);
        target.setUsername(targetUsername);
        target.setUpdateTime(new Date());

        int result = userInfoMapper.updateByPrimaryKey(target);

        return result;
    }

    public UserInfoVO findUserInfoByUsernameOrIdWithRolesAndPrivileges(String usernameOrId) {

        UserInfo userInfo = null;
        boolean foundOne = false;

        if (usernameOrId == null || "".equals(usernameOrId)) {
            log.error("【用户】 查询用户信息时， 没有收到正确的用户名或用户ID");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        userInfo = userInfoMapper.findOneWithRolesAndPrivilegesByUsernameOrId(usernameOrId, null);

        if (userInfo != null) {
            foundOne = true;
        } else {
            userInfo = userInfoMapper.findOneWithRolesAndPrivilegesByUsernameOrId(null, usernameOrId);
            if (userInfo != null) {
                foundOne = true;
            }
        }

        if (foundOne == true) {
            UserInfoVO userInfoVO = UserInfo2VO.convert(userInfo);
            return userInfoVO;
        }

        return null;
    }

    /**
     * 查找全部用户
     * 排除最高级账户（最高级账户ID：AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAA1）
     * @return
     */
    @Override
    public List<UserInfoVO> findAllUserInfos() {

        List<UserInfo> userInfos = userInfoMapper.selectAll();

        userInfos.removeIf(new Predicate<UserInfo>() {
            @Override
            public boolean test(UserInfo userInfo) {
                if ("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAA1".equals(userInfo.getUserId())) {
                    return true;
                }
               return false;
            }
        });

        List<UserInfoVO> userInfoVOs = userInfos.stream().map(e -> UserInfo2VO.convert(e)).collect(Collectors.toList());

        return userInfoVOs;
    }

}
