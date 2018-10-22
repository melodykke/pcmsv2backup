package com.zhsl.pcmsv2.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.convertor.dto2model.BaseInfoDTO2Model;
import com.zhsl.pcmsv2.convertor.tovo.BaseInfo2VO;
import com.zhsl.pcmsv2.dto.BaseInfoDTO;
import com.zhsl.pcmsv2.enums.RedisKeys;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.mapper.BaseInfoMapper;
import com.zhsl.pcmsv2.model.BaseInfo;
import com.zhsl.pcmsv2.model.Feedback;
import com.zhsl.pcmsv2.model.Region;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.service.BaseInfoService;
import com.zhsl.pcmsv2.service.RegionService;
import com.zhsl.pcmsv2.util.BeanUtils;
import com.zhsl.pcmsv2.util.RoleCheckUtil;
import com.zhsl.pcmsv2.util.UUIDUtils;
import com.zhsl.pcmsv2.vo.BaseInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class BaseInfoServiceImpl implements BaseInfoService {

    @Autowired
    private BaseInfoMapper baseInfoMapper;

    @Autowired
    private RegionService regionService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 删除水库
     *
     * @param baseInfoId
     * @return
     */
    @Override
    public int delete(String baseInfoId) {
        int result = baseInfoMapper.deleteByPrimaryKey(baseInfoId);
        return result;
    }

    @Override
    public int modify(BaseInfoDTO baseInfoDTO) {

        if (baseInfoDTO == null) {
            log.error("【基础信息】 修改水库基础信息时，收到数据 baseInfoDTO 为空");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        if (baseInfoDTO.getBaseInfoId() == null || "".equals(baseInfoDTO.getBaseInfoId())) {
            log.error("【基础信息】 修改水库基础信息时，收到数据 baseInfoDTO ID为空");
            throw new SysException(SysEnum.INVALID_KEY_RECEIVED_ERROR);
        }
        // 从数据库拿出记录
        BaseInfo baseInfo = baseInfoMapper.selectByPrimaryKey(baseInfoDTO.getBaseInfoId());
        // 保留ID 和 名称
        String baseInfoId = baseInfo.getBaseInfoId();
        String plantName = baseInfo.getPlantName();

        BeanUtils.copyProperties(baseInfoDTO, baseInfo);

        // 谁修改的 owner就是谁
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        baseInfo.setBaseInfoId(baseInfoId);
        baseInfo.setPlantName(plantName);
        baseInfo.setUpdateTime(new Date());
        baseInfo.setOwner(userInfo.getUsername());

        int result = baseInfoMapper.updateByPrimaryKey(baseInfo);

        return result;
    }

    /**
     * 查询所有水库的基础信息， 如果没有则返回空的容器。
     *
     * @return
     */
    @Override
    public List<BaseInfoVO> findAll() {

        // 首先尝试从redis中获取数据
        if (stringRedisTemplate.hasKey(RedisKeys.ALLBASEINFO.getKey())) {
            String allBaseInfoVOsStr = stringRedisTemplate.opsForValue().get(RedisKeys.ALLBASEINFO.getKey());
            if (allBaseInfoVOsStr != null && !"".equals(allBaseInfoVOsStr)) {
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, BaseInfoVO.class);
                try {
                    List<BaseInfoVO> baseInfoVOs = objectMapper.readValue(allBaseInfoVOsStr, javaType);
                    return baseInfoVOs;
                } catch (IOException e) {
                    log.error("【用户】 查询所有水库基础信息时， 从redis中获取的数据在json转化为对象时出错");
                }
            }
        }

        // 如果从redis中获取失败， 则尝试从数据库中获取数据
        List<BaseInfo> baseInfos = baseInfoMapper.selectAll();

        if (baseInfos == null) {
            log.info("【查询所有项目基础信息时，没有查找到任何记录】");
            return new ArrayList<>();
        }

        List<BaseInfoVO> baseInfoVOs = baseInfos.stream().map(e -> BaseInfo2VO.convert(e)).collect(Collectors.toList());

        return baseInfoVOs;
    }


    @Override
    public int create(BaseInfoDTO baseInfoDTO) {

        if (baseInfoDTO == null) {
            log.error("【基础信息】 创建水库基础信息时，收到的基础信息baseInfoDTO为空");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }

        List<String> allPlantName = baseInfoMapper.findAllPlantName();

        if (allPlantName.contains(baseInfoDTO.getPlantName())) {
            log.error("【基础信息】 创建水库基础信息时，收到的基础信息baseInfoDTO中的水库名字plantName在系统中已存在，" +
                    "不能重复申请 baseInfoDTO：", baseInfoDTO);
            throw new SysException(SysEnum.DUPLICATED_RECORD);
        }

        BaseInfo baseInfo = BaseInfoDTO2Model.convert(baseInfoDTO);

        // 项目法人 如果用户端提交过来法人名字则用，否则设置为信息提交人的账号名
        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (baseInfo.getOwner() == null || "".equals(baseInfo.getOwner())) {
            baseInfo.setOwner(thisUser.getUsername());
            ;
        }

        baseInfo.setBaseInfoId(UUIDUtils.getUUID());
        baseInfo.setCreateTime(new Date());
        baseInfo.setUpdateTime(new Date());

        int result = baseInfoMapper.insert(baseInfo);

        return result;
    }

    /**
     * 按照ID查找水库基础信息
     *
     * @param baseInfoId
     * @return
     */
    @Override
    public BaseInfoVO findById(String baseInfoId) {

        BaseInfo baseInfo = baseInfoMapper.selectByPrimaryKey(baseInfoId);

        if (baseInfo == null) {
            log.info("【查询某个项目基础信息时，没有查找到任何记录】");
            return new BaseInfoVO();
        }

        BaseInfoVO baseInfoVO = BaseInfo2VO.convert(baseInfo);

        return baseInfoVO;
    }

    @Override
    public List<BaseInfoVO> findByRegionId(HttpServletRequest request) {

        Region region = null;

        int regionId = 0;

        try {
            regionId = ServletRequestUtils.getIntParameter(request, "regionId")==null? 0 :
                    ServletRequestUtils.getIntParameter(request, "regionId");
        } catch (ServletRequestBindingException e) {
            log.info("【基础信息】 通过regionId查找区域水库基本信息时，没有接收到regionId参数");
        }

        UserInfo thisUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (regionId == 0) {
            region = thisUser.getRegion();
        } else if (regionId > 0) {
            if (RoleCheckUtil.checkIfPossessProvinceRole(thisUser)) {
                region = regionService.findByRegionId(regionId);

            }
        }

        if (region == null) {
            log.error("【基础信息】 按区域查询项目基础信息时，作查询操作的用户没有有效的区域信息");
            throw new SysException(SysEnum.PRECONDITION_MISSING_RECORD);
        }
        List<Region> childrenRegion = regionService.findChildrenRecursive(region.getRegionId());
        List<BaseInfo> baseInfos = baseInfoMapper.findByRegionsIn(childrenRegion);

        if (baseInfos == null) {

            return new ArrayList<>();
        }

        List<BaseInfoVO> baseInfoVOs = baseInfos.stream().map(e -> BaseInfo2VO.convert(e)).collect(Collectors.toList());

        return baseInfoVOs;
    }

    @Override
    public Feedback approveBaseInfo(UserInfo thisUser, Boolean switchState, String checkinfo, String baseInfoId) {
        return null;
    }

    /**
     * 将数据库中的所有baseInfo信息缓存至redis中 key：allBaseInfo
     */
    public void syncToRedis() {

        List<BaseInfo> baseInfos = baseInfoMapper.selectAll();
        List<BaseInfoVO> baseInfoVOs = baseInfos.stream().map(e -> BaseInfo2VO.convert(e)).collect(Collectors.toList());

        // 如果成功提交一个新的水库信息，则更新redis里所有水库的数据
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String allBaseInfoVOsStr = objectMapper.writeValueAsString(baseInfoVOs);
                stringRedisTemplate.opsForValue().set(RedisKeys.ALLBASEINFO.getKey(), allBaseInfoVOsStr);
            } catch (JsonProcessingException e) {
                log.error("水库基础信息同步至redis的过程中， 转化json出错。");
                return;
            }
        });
        log.error("水库基础信息同步至redis完成。");
    }
}
