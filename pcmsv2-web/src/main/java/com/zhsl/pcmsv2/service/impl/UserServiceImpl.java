package com.zhsl.pcmsv2.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.convertor.tovo.SysRole2VO;
import com.zhsl.pcmsv2.convertor.tovo.UserInfo2VO;
import com.zhsl.pcmsv2.dto.UserInfoDTO;
import com.zhsl.pcmsv2.dto.UsersRoles;
import com.zhsl.pcmsv2.enums.RedisKeys;
import com.zhsl.pcmsv2.enums.RoleEnum;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.mapper.RegionMapper;
import com.zhsl.pcmsv2.mapper.SysRoleMapper;
import com.zhsl.pcmsv2.mapper.SysUserRoleMapper;
import com.zhsl.pcmsv2.mapper.UserInfoMapper;
import com.zhsl.pcmsv2.model.Region;
import com.zhsl.pcmsv2.model.SysRole;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.service.RegionService;
import com.zhsl.pcmsv2.service.UserService;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.util.UUIDUtils;
import com.zhsl.pcmsv2.vo.SysRoleVO;
import com.zhsl.pcmsv2.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.zhsl.pcmsv2.convertor.dto2model.UserInfoDTO2Model.convert;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


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
        Set<Integer> allRegionIds = userInfoMapper.findAllRegionId();
        allRegionIds.removeIf(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                if (integer==0) return true;
                return false;
            }
        });

        if (allUsername.contains(userInfoDTO.getUsername())) {
            log.error("【用户】 创建用户时，已存在拟申请用户名");
            throw new SysException(SysEnum.DUPLICATED_RECORD);
        }
        if (allRegionIds.contains(userInfoDTO.getRegionId())) {
            log.error("【用户】 创建用户时，已存在拟申请用户所管辖区域的信息");
            throw new SysException(SysEnum.DUPLICATED_RECORD);
        }

        int regionId = userInfoDTO.getRegionId();
        Region region = null;

        if (regionId == 0) {
            region = new Region(0);
        } else {
            region = regionMapper.selectByPrimaryKey(regionId);
        }

        if (region == null) {
            log.error("【用户】 创建用户时，拟创建用户的地区错误");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        UserInfo userInfo = convert(userInfoDTO, region);

        userInfo.setUserId(UUIDUtils.getUUID());
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
        if (userInfoDTO.getRegionId() == null) {
            target.setRegion(new Region(0));
        } else {
            Region region = regionMapper.selectByPrimaryKey(userInfoDTO.getRegionId());
            if (region == null) {
                log.error("【用户】 修改用户时，拟修改的区域信息不存在！");
                throw new SysException(SysEnum.NOT_EXIST_RECORD);
            }
            target.setRegion(region);
        }


        int result = userInfoMapper.updateByPrimaryKey(target);

        return result;
    }

    /**
     * 封装了查找用户信息的参数username、 id 为一个参数 usernameOrId
     * @param usernameOrId
     * @return
     */
    @Override
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
    public List<UserInfoVO> findAll() {

        List<UserInfoVO> userInfoVOs = null;

        // 首先尝试从redis中获取数据
        if (stringRedisTemplate.hasKey(RedisKeys.ALLUSERINFO.getKey())) {
            String allUserInfoVosStr = stringRedisTemplate.opsForValue().get(RedisKeys.ALLUSERINFO.getKey());
            if (allUserInfoVosStr != null && !"".equals(allUserInfoVosStr)) {
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, UserInfoVO.class);
                try {
                    userInfoVOs = objectMapper.readValue(allUserInfoVosStr, javaType);
                    return userInfoVOs;
                } catch (IOException e) {
                    log.error("【用户】 查询所有用户信息时， 从redis中获取的数据在json转化为对象时出错");
                }
            }
        }

        // 如果redis中获取数据出错（可能是不存在）则尝试从数据库中获取
        List<UserInfo> userInfos = userInfoMapper.selectAll();

        if (userInfos == null) {
            return new ArrayList<>();
        }

        removeTopUser(userInfos);

        userInfoVOs = userInfos.stream().map(e -> UserInfo2VO.convert(e)).collect(Collectors.toList());

        return userInfoVOs;
    }

    /**
     *  获取系统所有角色 （排除工程管理部和建管处角色）
     * 只有最高级用户才能访问此方法
     * @return
     */
    @Override
    public List<SysRoleVO> findAllRoles() {

        List<SysRole> sysRoles = roleMapper.selectAll();
        sysRoles = removeTopRole(sysRoles);
        List<SysRoleVO> sysRoleVOs = sysRoles.stream().map(e -> SysRole2VO.convert(e)).collect(Collectors.toList());

        return sysRoleVOs;
    }

    /**
     * 批量添加用户和角色的关系
     * 该方法只有最高级管理员才能调用
     * @param usersRoles
     * @return
     */
    @Override
    public int batchInsertUsersRoles(UsersRoles usersRoles) {

        if (usersRoles.getUserIds() == null || usersRoles.getRoleIds() == null) {
            log.error("【权限设置】 权限设置时，没有收到有效的参数");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        List<String> userIds = usersRoles.getUserIds();
        List<String> rolesId = usersRoles.getRoleIds();

        if (userIds.size() == 0 || rolesId.size() == 0) {
            log.error("【权限设置】 权限设置时，没有收到有效的参数");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        List<UserInfo> userInfos = removeTopUser(userInfoMapper.selectAll());
        List<SysRole> sysRoles = removeTopRole(roleMapper.selectAll());

        List<String> userInfoIds = userInfos.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        List<String> sysRoleIds = sysRoles.stream().map(e -> e.getRoleId()).collect(Collectors.toList());

        // 循环每一个要设置权限的用户ID， 判断是否其在用户列表中，如果不在，则返回错误信息
        for (String userId : userIds) {
            if (!userInfoIds.contains(userId)) {
                log.error("【权限设置】 权限设置时，拟设置权限的用户并不存在于系统");
                throw new SysException(SysEnum.ILLEGAL_OPERATION);
            }
        }

        // 循环每一个要设置的角色ID， 判断是否其在系统角色列表中，如果不在，则返回错误信息
        for (String roleId : rolesId) {
            if (!sysRoleIds.contains(roleId)) {
                log.error("【权限设置】 权限设置时，拟设置权限的用户并不存在于系统");
                throw new SysException(SysEnum.ILLEGAL_OPERATION);
            }
        }

        // 循环要设置角色的用户， 如果用户之前已经有角色了， 则将其角色先删除后，再添加
        for (String userId : userIds) {
            userRoleMapper.deleteByUserId(userId);
        }

        int result = userRoleMapper.batchInsert(usersRoles);

        return result;
    }

    @Override
    public void syncToRedis() {

        List<UserInfo> userInfos = userInfoMapper.selectAll();

        removeTopUser(userInfos);

        List<UserInfoVO> userInfoVOs = userInfos.stream().map(e -> UserInfo2VO.convert(e)).collect(Collectors.toList());

        // 如果成功提交一个新的水库信息，则更新redis里所有水库的数据
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String allUserInfoVOsStr = objectMapper.writeValueAsString(userInfoVOs);
                stringRedisTemplate.opsForValue().set(RedisKeys.ALLUSERINFO.getKey(), allUserInfoVOsStr);
            } catch (JsonProcessingException e) {
                log.error("用户信息同步至redis的过程中， 转化json出错。");
                return;
            }
        });

        log.error("用户信息同步至redis完成。");
    }

    /**
     * 从容器中去掉顶级用户和工程管理部用户
     * @param userInfos
     * @return
     */
    protected List<UserInfo> removeTopUser(List<UserInfo> userInfos) {
        userInfos.removeIf(new Predicate<UserInfo>() {
            @Override
            public boolean test(UserInfo userInfo) {
                if ("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAA1".equals(userInfo.getUserId()) ||
                        "GCGLB567-0000-0000-0000-000000000000".equals(userInfo.getUserId())) {
                    return true;
                }
                return false;
            }
        });
        return userInfos;
    }

    /**
     * 从容器中去掉顶级用户和工程管理部角色
     * @param sysRoles
     * @return
     */
    protected List<SysRole> removeTopRole(List<SysRole> sysRoles) {

        sysRoles.removeIf(new Predicate<SysRole>() {
            @Override
            public boolean test(SysRole sysRole) {
                if (RoleEnum.DEPARTMENT.getKey().equals(sysRole.getRole()) ||
                        RoleEnum.PROVINCE.getKey().equals(sysRole.getRole()) ) {
                    return true;
                }
                return false;
            }
        });

        return sysRoles;
    }

}
