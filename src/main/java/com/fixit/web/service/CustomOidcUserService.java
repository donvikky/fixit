package com.fixit.web.service;

import com.fixit.web.entity.Profile;
import com.fixit.web.entity.User;
import com.fixit.web.enums.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomOidcUserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOidcUserService.class);
    private ProfileService profileService;
    private UserService userService;

    @Autowired
    public CustomOidcUserService(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    public void processUser(OidcUserRequest oidcUserRequest, OidcUser oidcUser){

        Optional<Profile> profileOptional = profileService.findByEmail(oidcUser.getEmail());
        Provider provider = getProvider(oidcUserRequest);
        User user;

        if(profileOptional.isPresent()){//email already exists
            user = profileOptional.get().getUser();
            upDateUserLogin(oidcUser, user);
        }else {//create new user and profile
            user = createNewUser(oidcUser, provider);
            createNewProfile(oidcUser, user);
        }

    }

    private Provider getProvider(OidcUserRequest request){
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

    private void upDateUserLogin(OidcUser oidcUser, User user){
        user.setLastLoginTime(LocalDateTime.now());
        userService.save(user);
    }

    private User createNewUser(OidcUser oidcUser, Provider provider){
        User user = new User();
        user.setUsername(oidcUser.getPreferredUsername());
        user.setPassword("");
        user.setProviderId(oidcUser.getSubject());
        user.setProvider(provider);
        user.setLastLoginTime(LocalDateTime.now());
        user.setEnabled(true);
        userService.save(user);
        return user;
    }

    private void createNewProfile(OidcUser oidcUser, User user){
        Profile profile = new Profile();
        profile.setFirstName(oidcUser.getGivenName());
        profile.setLastName(oidcUser.getFamilyName());
        profile.setEmail(oidcUser.getEmail());
        profile.setMobileNumber(oidcUser.getPhoneNumber());
        profile.setUser(user);
        profileService.save(profile);
    }

}
