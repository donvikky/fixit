package com.fixit.web.service;

import com.fixit.web.auth.CustomOauth2User;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.Role;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private ProfileService profileService;
    private UserService userService;

    private RoleService roleService;

    @Autowired
    public CustomOAuth2UserService(ProfileService profileService, UserService userService, RoleService roleService) {
        this.profileService = profileService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        CustomOauth2User customOauth2User = new CustomOauth2User(oAuth2User);
        processUser(oAuth2UserRequest, customOauth2User);
        return customOauth2User;
    }

    public void processUser(OAuth2UserRequest oAuth2UserRequest, CustomOauth2User oAuth2User){
        logger.info("Beginning user processing");
        Optional<Profile> profileOptional = profileService.findByEmail(oAuth2User.getEmail());
        Provider provider = getProvider(oAuth2UserRequest);
        User user;

        System.out.println(oAuth2User.getAttributes());

        if(profileOptional.isPresent()){//email already exists
            user = profileOptional.get().getUser();
            upDateUserLogin(oAuth2UserRequest, user);
        }else {//create new user and profile
            user = createNewUser(oAuth2User, provider);
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
        userService.save(user);
    }

    private User createNewUser(CustomOauth2User oauth2User, Provider provider){
        Optional<Role> userRoleOptional = roleService.findByName("ROLE_USER");
        Role userRole = userRoleOptional.orElseThrow(() -> new NoSuchElementException("The specified role does not exist"));

        User user = new User();
        user.setUsername(oauth2User.getEmail());
        user.setPassword("");
        user.setProviderId(oauth2User.getAttribute("id"));
        user.setProvider(provider);
        user.setLastLoginTime(LocalDateTime.now());
        user.setRoles(List.of(userRole));
        user.setEnabled(true);
        userService.save(user);
        return user;
    }

    private void createNewProfile(CustomOauth2User oAuth2User, User user){
        Profile profile = new Profile();
        profile.setFirstName(oAuth2User.getName().split(" ")[0]);
        profile.setLastName(oAuth2User.getName().split(" ")[1]);
        profile.setEmail(oAuth2User.getEmail());
        profile.setMobileNumber("0123456789");
        profile.setUser(user);
        profileService.save(profile);
    }
}
