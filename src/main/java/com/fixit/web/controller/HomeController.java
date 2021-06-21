package com.fixit.web.controller;

import com.fixit.web.entity.Craft;
import com.fixit.web.model.ProfileSearch;
import com.fixit.web.service.CraftService;
import com.fixit.web.service.UserService;
import com.fixit.web.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private CraftService craftService;
    private AuthUtils authUtils;

    @Autowired
    public HomeController(CraftService craftService, UserService userService, AuthUtils authUtils) {
        this.craftService = craftService;
        this.authUtils = authUtils;
    }

    @GetMapping
    public String home(Model model){
        ProfileSearch search = new ProfileSearch();
        List<Craft> crafts = craftService.listAll();
        model.addAttribute("search", search);
        model.addAttribute("crafts", crafts);
        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        System.out.println("user roles" + authUtils.getCurrentUser().get().getRoles());
        return "dashboard";
    }

    @GetMapping("/oauth")
    public String oauthLogin(){
        return "auth/oauth";
    }
}
