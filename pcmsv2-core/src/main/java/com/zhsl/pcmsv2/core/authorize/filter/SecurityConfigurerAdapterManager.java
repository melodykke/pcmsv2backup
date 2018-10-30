package com.zhsl.pcmsv2.core.authorize.filter;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface SecurityConfigurerAdapterManager {

    void apply(HttpSecurity http);
}
