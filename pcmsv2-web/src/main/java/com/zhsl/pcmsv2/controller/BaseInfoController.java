package com.zhsl.pcmsv2.controller;

import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.browser.support.ResultVO;
import com.zhsl.pcmsv2.browser.util.ResultUtil;
import com.zhsl.pcmsv2.dto.BaseInfoDTO;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.service.BaseInfoService;
import com.zhsl.pcmsv2.vo.BaseInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/baseinfo")
public class BaseInfoController {

    @Autowired
    private BaseInfoService baseInfoService;


    @PutMapping("/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    public ResultVO update(@PathVariable String id, @RequestBody BaseInfoDTO baseInfoDTO) {

        if (baseInfoDTO == null) {
            log.error("【基础信息】 修改项目基础信息时，收到的baseInfoDTO为空");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        baseInfoDTO.setBaseInfoId(id);

        int result = baseInfoService.modify(baseInfoDTO);

        if (result == 1) {
            baseInfoService.syncToRedis();
            return ResultUtil.success();
        } else {
            return ResultUtil.failed();
        }
    }

    @PostMapping
    public ResultVO create(@Valid @RequestBody BaseInfoDTO baseInfoDTO, BindingResult bindingResult) {

        if (baseInfoDTO == null) {
            log.error("【基础信息】 收到的基础信息出错，baseInfoDTO为空");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        if (bindingResult.hasErrors()) {
            log.error("【基础信息】 收到的基础信息出错，错误信息为：{}", bindingResult.getFieldError().getDefaultMessage());
            throw new SysException(SysEnum.VERIFY_RECEIVED_INFO_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        int result = baseInfoService.create(baseInfoDTO);

        if (result == 1) {
            baseInfoService.syncToRedis();
            return ResultUtil.success();
        } else {
            return ResultUtil.failed();
        }
    }

    @GetMapping("/byregion/{id:\\d+}")
    public ResultVO getByRegionId(@PathVariable int id) {

        List<BaseInfoVO> baseInfoVOs = baseInfoService.findByRegionId(id);

        return ResultUtil.success(baseInfoVOs);
    }

    /**
     *  管理员们才能访问
     * @param id
     * @return
     */
    @GetMapping("/management/{id:[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}}")
    public ResultVO getBaseInfo(@PathVariable String id) {

        BaseInfoVO baseInfoVO = baseInfoService.findById(id);

        return ResultUtil.success(baseInfoVO);
    }

    @GetMapping("/mybaseinfo")
    public ResultVO getMybaseinfo() {

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String myBaseInfoId = thisUser.getBaseInfoId();

        if (myBaseInfoId == null || "".equals(myBaseInfoId)) {
           log.error("【基础信息】 用户查询自己的水库基础信息时出错， 自己的baseInfoId为空");
           throw new SysException(SysEnum.NOT_EXIST_RECORD);
        }

        BaseInfoVO baseInfoVO = baseInfoService.findById(myBaseInfoId);

        return ResultUtil.success(baseInfoVO);
    }

    @GetMapping("/all")
    public ResultVO getAll() {

        List<BaseInfoVO> baseInfoVOs = baseInfoService.findAll();

        return ResultUtil.success(baseInfoVOs);
    }


}
