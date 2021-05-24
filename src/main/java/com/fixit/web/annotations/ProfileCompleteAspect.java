package com.fixit.web.annotations;

import com.fixit.web.entity.Profile;
import com.fixit.web.utils.AuthUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ProfileCompleteAspect {

    private AuthUtils authUtils;

    public ProfileCompleteAspect(AuthUtils authUtils){
        this.authUtils = authUtils;
    }

    @Before(value = "@annotation(ProfileComplete)")
    public void profileCompleted(JoinPoint joinPoint) throws Exception{
        Profile profile = authUtils.getCurrentUser().get().getProfile();
        if(isInvalidProfile(profile)){
            throw new Exception("Please complete your profile first");
        }
    }
    private Boolean isInvalidProfile(Profile profile){
        return profile == null || profile.getAddress() == null || profile.getAddress() == "";
    }
}
