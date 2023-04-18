package com.fixit.web.service;

import com.fixit.web.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    @Autowired
    private AuthUtils authUtils;
    public boolean hasProfile(){
        return authUtils.getCurrentUser().get().getProfile() != null;
    }
}
