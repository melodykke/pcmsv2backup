package com.zhsl.pcmsv2.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    PROVINCE("ROLE_PROVINCE"),
    PLP("ROLE_PLP"),
    ;

    private String key;

    RoleEnum(String key) {
        this.key = key;
    }

}
