package com.zhsl.pcmsv2.controller;

import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.browser.support.ResultVO;
import com.zhsl.pcmsv2.browser.util.ResultUtil;
import com.zhsl.pcmsv2.dto.UserInfoDTO;
import com.zhsl.pcmsv2.dto.UsersRoles;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.service.UserService;
import com.zhsl.pcmsv2.vo.SysRoleVO;
import com.zhsl.pcmsv2.vo.UserInfoVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    @ApiOperation(value = "创建用户")
    public ResultVO create(@Valid @RequestBody UserInfoDTO userInfoDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("【用户】 用户创建失败！ 收到信息校验失败！ bindingResult：{}",
                    bindingResult.getFieldError().getDefaultMessage());
            throw new SysException(SysEnum.VERIFY_RECEIVED_INFO_ERROR.getCode(), bindingResult.getFieldError()
                    .getDefaultMessage());
        }

        int result = userService.create(userInfoDTO);

        if (result == 1) {
            userService.syncToRedis();
            return ResultUtil.success();
        } else {
            return ResultUtil.failed();
        }
    }

    @DeleteMapping("/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    public ResultVO delete(@PathVariable String id) {

        int result = userService.delete(id);

        if (result == 1) {
            userService.syncToRedis();
            return ResultUtil.success();
        } else {
            return ResultUtil.failed();
        }
    }

    @PutMapping("/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    public ResultVO update(@PathVariable String id, @RequestBody UserInfoDTO userInfoDTO) {

        if (id == null || "".equals(id)) {
            log.error("【用户】 更新用户信息时， 用户主键为空");
            throw  new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }

        userInfoDTO.setUserId(id);

        int result = userService.modify(userInfoDTO);

        if (result == 1) {
            userService.syncToRedis();
            return ResultUtil.success();
        } else {
            return ResultUtil.failed();
        }
    }

    @GetMapping("/getUserInfoByUserNameOrId/{usernameOrId}")
    @ApiOperation(value = "通过用户名或ID查询用户的服务 包含用户的角色和权限信息")
    public ResultVO getUserInfoByUserNameOrId(@PathVariable String usernameOrId) {

        UserInfoVO userInfoVO = userService.findUserInfoByUsernameOrIdWithRolesAndPrivileges(usernameOrId);

        if (userInfoVO == null) {
            return ResultUtil.failed();
        }

        return ResultUtil.success(userInfoVO);
    }

    @GetMapping("/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    public UserInfoVO getUserInfo(@PathVariable String id) {
        UserInfoVO userInfoVO = userService.findById(id);
        return userInfoVO;
    }

    @GetMapping("/all")
    @ApiOperation(value = "获取不包括最上级用户的所有用户信息")
    public ResultVO getAll() {

        List<UserInfoVO> userInfoVOs = userService.findAll();

        if (userInfoVOs == null) {
            return ResultUtil.failed();
        }

        return ResultUtil.success(userInfoVOs);
    }

    @PostMapping("/management/batchsetroles")
    public ResultVO batchSetRoles(@RequestBody UsersRoles usersRoles) {

        int result = userService.batchInsertUsersRoles(usersRoles);

        return ResultUtil.success(result);
    }

    @GetMapping("/management/allroles")
    public ResultVO getAllRoles() {

        List<SysRoleVO> sysRoleVOs = userService.findAllRoles();

        if (sysRoleVOs.size() > 0) {
            return ResultUtil.success(sysRoleVOs);
        } else {
            return ResultUtil.failed();
        }
    }

}
