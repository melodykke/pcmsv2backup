package com.zhsl.pcmsv2.service;

import com.zhsl.pcmsv2.model.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserInfo findById(String id);
}
