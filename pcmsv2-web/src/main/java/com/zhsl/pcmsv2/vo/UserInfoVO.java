package com.zhsl.pcmsv2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhsl.pcmsv2.model.Region;
import com.zhsl.pcmsv2.model.SysRole;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoVO {
    private String userId;

    private Date createTime;

    private String name;

    private String password;

    private Date updateTime;

    private String username;

    private String baseInfoId;

    private String parentId;

    private String openId;

    private List<SysRoleVO> roleVOs;

    private boolean accountNonLocked;

    private RegionVO regionVO;
}
