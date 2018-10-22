package com.zhsl.pcmsv2.service;

import com.zhsl.pcmsv2.dto.BaseInfoDTO;
import com.zhsl.pcmsv2.model.Feedback;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.vo.BaseInfoVO;

import java.util.List;

public interface BaseInfoService {

    /**
     * 删除指定ID的水库基本信息
     * @param baseInfoId
     * @return
     */
    int delete(String baseInfoId);

    int modify(BaseInfoDTO baseInfoDTO);

    /**
     * 查找全部水库基础信息的方法
     * @return
     */
    List<BaseInfoVO> findAll();

    /**
     * 创建水库基本信息
     * @param baseInfoDTO
     * @return
     */
    int create(BaseInfoDTO baseInfoDTO);

    /**
     * 根据ID查找具体水库信息
     * @param baseInfoId
     * @return
     */
    BaseInfoVO findById(String baseInfoId);

    /**
     * 根据区域查询属于该区域的水库群
     * @param regionId
     * @return
     */
    List<BaseInfoVO> findByRegionId(Integer regionId);

    /**
     * 将数据库中的所有baseInfo信息缓存至redis中 key：allBaseInfo
     */
    void syncToRedis();


    Feedback approveBaseInfo(UserInfo thisUser, Boolean switchState, String checkinfo, String baseInfoId);

}