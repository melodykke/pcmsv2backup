package com.zhsl.pcmsv2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthDataDTO {
    private String clientId;
    private String orgName;
    private String accessUrl;
    private String appName;
    private Integer sex;
    private String appCode;
    private Integer curStatus;
    private String userName;
    private String orgType;
    private String orgCode;
    private String loginName;
    private String callbackUrl;
    private String company;
    private String grantType;
    private Integer userAccountType;
    private String account;
}
