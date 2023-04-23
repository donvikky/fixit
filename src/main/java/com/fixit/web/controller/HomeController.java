package com.fixit.web.controller;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.User;
import com.fixit.web.model.ProfileSearch;
import com.fixit.web.service.*;
import com.fixit.web.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private CraftService craftService;
    private BidService bidService;
    private UserService userService;
    private JobService jobService;
    private JobReviewService jobReviewService;
    private AuthUtils authUtils;

    @Autowired
    public HomeController(CraftService craftService, UserService userService, AuthUtils authUtils,
                          BidService bidService, JobService jobService, JobReviewService jobReviewService) {
        this.craftService = craftService;
        this.authUtils = authUtils;
        this.bidService = bidService;
        this.userService = userService;
        this.jobService = jobService;
        this.jobReviewService = jobReviewService;
    }

    @GetMapping("/")
    public String home(Model model){
        ProfileSearch search = new ProfileSearch();
        List<Craft> crafts = craftService.listAll();
        List<Job> recentJobs = jobService.findMostRecentJobs();
        model.addAttribute("search", search);
        model.addAttribute("crafts", crafts);
        model.addAttribute("recentJobs", recentJobs);
        return "home";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        Profile profile = authUtils.getCurrentUser().get().getProfile();
        int bidsWon = bidService.getBidsWon(profile);
        int postedJobsCount = jobService.getPostedJobsCount(profile);
        int jobReviewsCount = jobReviewService.findJobReviewsCount(profile);
        System.out.println("Posted Jobs: "+ postedJobsCount);
        model.addAttribute("bidsWon", bidsWon);
        model.addAttribute("postedJobsCount", postedJobsCount);
        model.addAttribute("jobReviewsCount", jobReviewsCount);
        model.addAttribute("profile", profile);
        return "dashboard";
    }

    @GetMapping("/oauth")
    public String oauthLogin(){
        return "auth/oauth";
    }

    @GetMapping("verify/user/{token}")
    public String verifyUser(@PathVariable("token") String token, Model model){
        Optional<User> user = userService.findByVerificationToken(token);
        if(user.isEmpty()){
            model.addAttribute("error",
                    "An error was encountered. Please request a valid token.");
        } else if (user.get().getEnabled()) {
            model.addAttribute("warning",
                    "Your account has been previously verified.");
        }else{
            //update user
            userService.updateUserByVerificationToken(token);
            model.addAttribute("success",
                    "Your registration has been verified successfully. Please login to access your account");
        }
        return "verify";
    }

    @GetMapping("/privacy")
    public String privacyPolicyPage(){
        return "privacy";
    }

    @GetMapping("/terms")
    public String termsAndConditions(){
        return "terms";
    }
}
