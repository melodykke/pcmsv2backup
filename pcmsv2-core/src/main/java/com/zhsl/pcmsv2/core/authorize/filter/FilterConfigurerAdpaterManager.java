package com.zhsl.pcmsv2.core.authorize.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FilterConfigurerAdpaterManager implements SecurityConfigurerAdapterManager {

    private Logger log = LoggerFactory.getLogger(FilterConfigurerAdpaterManager.class);

    @Autowired
    private Set<SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>> securityConfigurerAdapter;

    @Override
    public void apply(HttpSecurity http) {
        for (SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> securityConfigurerAdapter : securityConfigurerAdapter) {
            try {
                http.apply(securityConfigurerAdapter);
            } catch (Exception e) {
                log.error("配置自定义过滤器出错！");
            }
        }
    }
}
