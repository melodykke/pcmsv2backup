package com.zhsl.pcmsv2.authorize;

import com.zhsl.pcmsv2.core.authorize.AuthorizeConfigProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
public class Pcmsv2AuthorizeConfigProvider implements AuthorizeConfigProvider{

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers("/user/getall")
                .hasRole("PROVINCE");
    }
}
