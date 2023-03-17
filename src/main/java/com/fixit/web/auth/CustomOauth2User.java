package com.fixit.web.auth;

import com.fixit.web.entity.User;
import com.fixit.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomOauth2User implements OAuth2User {

    private OAuth2User oAuth2User;
    private String accessToken;

    @Autowired
    private UserService userService;
    private List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

    public CustomOauth2User(OAuth2User oAuth2User){
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getEmail() {
        return oAuth2User.<String>getAttribute("email");
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Optional<User> getUser(){
        return userService.findByProviderId(oAuth2User.getAttribute("id"));
    }

}
