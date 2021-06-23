package com.fixit.web.controller;

import com.fixit.web.entity.Craft;
import com.fixit.web.model.ProfileSearch;
import com.fixit.web.service.BidService;
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
    private BidService bidService;
    private UserService userService;
    private AuthUtils authUtils;

    @Autowired
    public HomeController(CraftService craftService, UserService userService, AuthUtils authUtils,
                          BidService bidService) {
        this.craftService = craftService;
        this.authUtils = authUtils;
        this.bidService = bidService;
        this.userService = userService;
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
        //Profile profile = new AuthUtils(userService).getCurrentUser().get().getProfile();
        //System.out.println(profile);
        //model.addAttribute("bidsWon");
        return "dashboard";
    }

    @GetMapping("/oauth")
    public String oauthLogin(){
        return "auth/oauth";
    }
}
