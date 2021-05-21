package com.fixit.web.service;

import com.fixit.web.auth.CustomOauth2User;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.User;
import com.fixit.web.enums.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private ProfileService profileService;
    private UserService userService;

    @Autowired
    public CustomOAuth2UserService(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        CustomOauth2User customOauth2User = new CustomOauth2User(oAuth2User);
        processUser(oAuth2UserRequest, customOauth2User);
        System.out.println(customOauth2User.getName());
        return customOauth2User;
    }

    public void processUser(OAuth2UserRequest oAuth2UserRequest, CustomOauth2User oAuth2User){
        logger.info("Beginning user processing");
        Optional<Profile> profileOptional = profileService.findByEmail(oAuth2User.getEmail());
        Provider provider = getProvider(oAuth2UserRequest);
        User user;

        if(profileOptional.isPresent()){//email already exists
            user = profileOptional.get().getUser();
            upDateUserLogin(oAuth2UserRequest, user);
        }else {//create new user and profile
            user = createNewUser(oAuth2UserRequest, provider);
            createNewProfile(oAuth2User, user);
        }

    }

    private Provider getProvider(OAuth2UserRequest request){

        Provider provider = null;
        switch (request.getClientRegistration().getRegistrationId().toLowerCase()){
            case "google":
                provider = Provider.GOOGLE;
                break;
            case "facebook":
                provider = Provider.FACEBOOK;
                break;
            default:
                provider = Provider.LOCAL;
        }

        return provider;
    }

    private void upDateUserLogin(OAuth2UserRequest oAuth2UserRequest, User user){
        user.setLastLoginTime(LocalDateTime.now());
        user.setToken(oAuth2UserRequest.getAccessToken().getTokenValue());
        userService.save(user);
    }

    private User createNewUser(OAuth2UserRequest oAuth2UserRequest, Provider provider){
        User user = new User();
        user.setUsername("");
        user.setPassword("");
        user.setToken(oAuth2UserRequest.getAccessToken().getTokenValue());
        user.setProvider(provider);
        user.setLastLoginTime(LocalDateTime.now());
        user.setEnabled(true);
        userService.save(user);
        return user;
    }

    private void createNewProfile(CustomOauth2User oAuth2User, User user){
        Profile profile = new Profile();
        profile.setFirstName(oAuth2User.getName().split(" ")[0]);
        profile.setLastName(oAuth2User.getName().split(" ")[1]);
        profile.setEmail(oAuth2User.getEmail());
        profile.setMobileNumber("");
        profile.setUser(user);
        profileService.save(profile);
    }
}
