package com.zhsl.pcmsv2.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;


@Data
public class UserInfoDTO {

    private String userId;

    private boolean accountNonLocked = true;

    @NotBlank(message = "水库名不能为空！")
    @Size(min = 4, max = 12, message = "水库名必须由4至12个汉字组成")
    private String name;

    @NotBlank(message = "密码不能为空！")
    @Size(min = 4, max = 12, message = "密码必须由4至12个数字、字母或特殊字符组成")
    private String password;

    @NotBlank(message = "确认密码不能为空！")
    @Size(min = 4, max = 12, message = "密码必须由4至12个数字、字母或特殊字符组成")
    private String rePassword;

    @NotBlank(message = "账号名不能为空！")
    @Size(min = 4, max = 12, message = "账号必须由4至12个字母组成")
    private String username;

    @NotNull(message = "用户所在地区不能为空！")
    private Integer regionId;

    @NotNull(message = "用户所属上级不能为空！")
    private String parentId;
}
