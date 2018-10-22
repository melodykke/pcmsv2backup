package com.zhsl.pcmsv2.enums;

import lombok.Getter;

@Getter
public enum RedisKeys {
    ALLUSERINFO("allUserInfo"),
    ALLBASEINFO("allBaseInfo"),
    ALLPMR("allPmr")
    ;

    private String key;

    RedisKeys(String key) {
        this.key = key;
    }

}
