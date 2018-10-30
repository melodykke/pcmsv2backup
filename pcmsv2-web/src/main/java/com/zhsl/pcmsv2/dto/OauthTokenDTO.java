package com.zhsl.pcmsv2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthTokenDTO {
    private OauthMetaDTO meta;
    private OauthDataDTO data;
}
