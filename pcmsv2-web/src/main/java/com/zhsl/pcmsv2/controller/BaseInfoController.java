package com.zhsl.pcmsv2.controller;

import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.browser.support.ResultVO;
import com.zhsl.pcmsv2.browser.util.ResultUtil;
import com.zhsl.pcmsv2.dto.BaseInfoDTO;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.service.BaseInfoService;
import com.zhsl.pcmsv2.vo.BaseInfoVO;
import com.zhsl.pcmsv2.vo.LifeCircle;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
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

    /**
     * 可配置参数为"regionId"
     * @param request
     * @return
     */
    @GetMapping("/byregion")
    @ApiOperation(value = "按登陆用户所在区域获取所有水库基础信息 (management)")
    @ApiImplicitParam(name = "regionId", value = "区域ID 此项只能最高级用户才能传参", required = false, dataType = "Integer")
    public ResultVO getByRegionId(HttpServletRequest request) {

        List<BaseInfoVO> baseInfoVOs = baseInfoService.findByRegionId(request);

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

    /**
     * 用户自己更新自己水库的动工时间
     * 如果水库的动工时间节点状态还处于动工前（开始或项目前期）则不能更改动工时间
     * @param commenceDate
     * @return
     */
    @PutMapping("/commencedate")
    public ResultVO updateCommenceDate(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date commenceDate) {

        int result = baseInfoService.updateCommenceDate(commenceDate);

        if (result == 1) {
            return ResultUtil.success();
        } else {
            return ResultUtil.failed();
        }

    }

    /**
     * 获取自己的水库信息
     * 只有地方业主 PLP 才能访问
     * @return
     */
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

    /**
     * 管理员获取其所管辖区域内水库的生命中周期相关信息
     * @return
     */
    @GetMapping("lifecircle")
    @ApiOperation(value = "按登陆用户获取水库生命周期信息 如果是管理员则返回辖区内某个水库的生命周期 (management)")
    @ApiImplicitParam(name = "baseInfoId", value = "水库ID 此项只能最高级用户才能传参", required = false, dataType = "String")
    public ResultVO lifeCircle(@RequestParam(name = "baseInfoId", required = false) String baseInfoId) {

        LifeCircle lifeCircle = baseInfoService.getLifeCircle(baseInfoId);

        return ResultUtil.success(lifeCircle);
    }
}
