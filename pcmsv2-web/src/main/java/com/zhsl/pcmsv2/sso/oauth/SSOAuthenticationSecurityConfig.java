package com.zhsl.pcmsv2.sso.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class SSOAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private UserDetailsService myUserDetailsService;

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SSOAuthenticationFilter sSOAuthenticationFilter = new SSOAuthenticationFilter();
        sSOAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        sSOAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        sSOAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        SSOAuthenticationProvider sSOAuthenticationProvider = new SSOAuthenticationProvider();
        sSOAuthenticationProvider.setMyUserDetailsService(myUserDetailsService);

        http.authenticationProvider(sSOAuthenticationProvider) // 把自定义的authenticationProvider加入到安全框架的provider集合中
            .addFilterBefore(sSOAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
