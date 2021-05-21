package com.fixit.web.utils;

import com.fixit.web.auth.CustomUserDetails;
import com.fixit.web.entity.User;
import com.fixit.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthUtils {

    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthUtils.class);

    /*
    public User getCurrentUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUser();
    }
    */
    public Optional<User> getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticationInvalid(authentication)) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication;
            return Optional.of(userDetails.getUser());
        }
        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            Optional<User> user = userService.findByToken(oidcUser.getIdToken().getTokenValue());
            return user;
        }
        return Optional.of((User) authentication.getPrincipal());
    }

    private static boolean isAuthenticationInvalid(final Authentication authentication) {
        return authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null;
    }

}
