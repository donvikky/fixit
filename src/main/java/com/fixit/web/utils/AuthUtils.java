package com.fixit.web.utils;

import com.fixit.web.auth.CustomOauth2User;
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
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUtils {

    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthUtils.class);

    @Autowired
    public AuthUtils(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAuthenticationInvalid(authentication)) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return Optional.of(userDetails.getUser());
        }
        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            return userService.findByProviderId(oidcUser.getSubject());
        }
        if (authentication.getPrincipal() instanceof OAuth2User) {
            CustomOauth2User oauth2User = (CustomOauth2User) authentication.getPrincipal();
            return userService.findByProviderId(oauth2User.getAttribute("id"));
        }

        return Optional.of((User) authentication.getPrincipal());

    }

    private static boolean isAuthenticationInvalid(final Authentication authentication) {
        return authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null;
    }

}
