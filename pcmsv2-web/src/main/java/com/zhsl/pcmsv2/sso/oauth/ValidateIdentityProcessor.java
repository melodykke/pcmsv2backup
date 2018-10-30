package com.zhsl.pcmsv2.sso.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhsl.pcmsv2.dto.OauthTokenDTO;
import com.zhsl.pcmsv2.exception.SSOAuthenticationException;
import com.zhsl.pcmsv2.util.OauthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ValidateIdentityProcessor {

    // 返回服务器端验证通过的用户名
    public String validate(String sSOAuthenticationUrl ,String accessToken) {

        String username = "";

        String rtStr = OauthUtil.sendGet(sSOAuthenticationUrl, accessToken);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            OauthTokenDTO oauthTokenDTO = objectMapper.readValue(rtStr, OauthTokenDTO.class);

            if (oauthTokenDTO.getMeta().getCode() != 1) {
                log.error("单点登录未验证通过 oauthTokenDTO：", oauthTokenDTO);
                throw new SSOAuthenticationException("SSO认证失败！");
            }
            username = oauthTokenDTO.getData().getLoginName();
            // 青云的认证通过 则拿到用户名在本系统里获取权限
            if (username == null || "".equals(username)) {
                log.error("单点登录验证通过，但为获取到用户名！");
                throw new SSOAuthenticationException("SSO认证失败！");
            }
        } catch (IOException e) {
            log.error("单点登录认证时，返回的用户身份信息数据格式不符合要求，json转换失败!");
            throw new SSOAuthenticationException("SSO认证时，身份信息转化失败！");
        }

        return username;
    }

}
