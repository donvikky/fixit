package com.fixit.web.utils;

import com.fixit.web.auth.CustomUserDetails;
import com.fixit.web.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public AuthUtils(){

    }

    public User getCurrentUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUser();
    }
}
