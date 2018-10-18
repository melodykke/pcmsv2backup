package com.zhsl.pcmsv2.vo;

import com.zhsl.pcmsv2.model.SysRole;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
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

    private List<SysRole> roles;

    private boolean accountNonLocked;
}
