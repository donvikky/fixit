package com.fixit.web.audit;

import com.fixit.web.auth.CustomOauth2User;
import com.fixit.web.auth.CustomOidcUser;
import com.fixit.web.auth.CustomUserDetails;
import com.fixit.web.service.ProfileService;
import com.fixit.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;
    @Override
    public Optional<String> getCurrentAuditor() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticationInvalid(authentication)) {
            return Optional.empty();
        }
        return getCurrentAuditor(authentication);
    }

    private Optional<String> getCurrentAuditor(final Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return Optional.of(userDetails.getUser().getId().toString());
        }
        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            CustomOidcUser customOidcUser = new CustomOidcUser(oidcUser, userService);
            return Optional.of(customOidcUser.getUser().get().getId().toString());
        }
        if (authentication.getPrincipal() instanceof OAuth2User) {
            CustomOauth2User oauth2User = (CustomOauth2User) authentication.getPrincipal();
            return Optional.of(userService.findByProviderId(oauth2User.getAttribute("id")).get().getId().toString());
        }
        return Optional.of(authentication.getPrincipal().toString());
    }

    private static boolean isAuthenticationInvalid(final Authentication authentication) {
        return authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null;
    }
}
