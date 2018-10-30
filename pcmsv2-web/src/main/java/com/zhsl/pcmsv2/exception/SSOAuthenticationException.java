package com.zhsl.pcmsv2.exception;

import org.springframework.security.core.AuthenticationException;

public class SSOAuthenticationException extends AuthenticationException {

    public SSOAuthenticationException(String msg) {
        super(msg);
    }

}
