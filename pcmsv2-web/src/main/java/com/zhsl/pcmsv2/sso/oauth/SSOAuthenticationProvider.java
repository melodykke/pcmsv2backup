package com.zhsl.pcmsv2.sso.oauth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SSOAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService myUserDetailsService;

    /**
     * 调用自定义UserDetailsService验证处理逻辑
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernameAuthenticationToken authenticationToken = (UsernameAuthenticationToken) authentication;
        UserDetails user = myUserDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        UsernameAuthenticationToken authenticationResult =  new UsernameAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authentication.getDetails());
        return authenticationResult;
    }

    /**
     * 判断该种token的认证方式是否能被此provider处理
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernameAuthenticationToken.class.isAssignableFrom(authentication);
    }


    public UserDetailsService getMyUserDetailsService() {
        return myUserDetailsService;
    }

    public void setMyUserDetailsService(UserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }
}