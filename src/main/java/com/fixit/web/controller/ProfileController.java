package com.fixit.web.controller;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.JobReview;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.State;
import com.fixit.web.model.ProfileSearch;
import com.fixit.web.service.*;
import com.fixit.web.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profiles")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private ProfileService profileService;
    private StateService stateService;

    private FileStorageService storageService;
    private CraftService craftService;
    private JobService jobService;

    private BidService bidService;

    private JobReviewService jobReviewService;
    private AuthUtils authUtils;

    @Autowired
    public ProfileController(ProfileService profileService, FileStorageService storageService, StateService stateService,
                             CraftService craftService, AuthUtils authUtils, JobService jobService, BidService bidService,
                             JobReviewService jobReviewService) {
        this.profileService = profileService;
        this.storageService = storageService;
        this.stateService = stateService;
        this.craftService = craftService;
        this.authUtils = authUtils;
        this.jobService = jobService;
        this.bidService = bidService;
        this.jobReviewService  = jobReviewService;
    }

    /*
     * Ensures the states variable is available in all templates
     * in this controller
     */
    @ModelAttribute("states")
    public List<State> getStates(){
        return stateService.listAll();
    }

    /*
     * Ensures the crafts variable is available in all templates
     * in this controller
     */
    @ModelAttribute("crafts")
    public List<Craft> getCrafts(){
        return craftService.listAll();
    }

    @GetMapping
    public String index(Model model){
        List<Profile> profiles = profileService.listAll();
        model.addAttribute("profiles", profiles);
        return "profiles/index";
    }

    @GetMapping("/create")
    public String add(Model model){
        Profile profile = profileService.findByUser(authUtils.getCurrentUser().get());
        if(profile != null){
            return "redirect:/profiles/edit";
        }
        profile = new Profile();
        model.addAttribute("profile", profile);
        return "profiles/create";
    }

    @PostMapping("/create")
    public String save(@Valid Profile profile, BindingResult bindingResult,
                       @RequestParam("file") MultipartFile file,
                       SessionStatus sessionStatus){
        if(bindingResult.hasErrors() || file.isEmpty()){
            return "profiles/create";
        }
        String fileName = storageService.save(file);
        
        profile.setUser(authUtils.getCurrentUser().get());
        profile.setPhoto(fileName);
        profileService.save(profile);
        sessionStatus.setComplete();
        return "redirect:/dashboard";
    }

    @GetMapping("/edit")
    public String edit(Model model){

        Profile profile = profileService.findByUser(authUtils.getCurrentUser().get());
        if(profile == null){
            return "redirect:/profiles/create";
        }
        model.addAttribute("profile", profile);
        return "profiles/edit";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        Profile profile = profileService.get(id);

        if(profile == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("profile", profile);
        return "profiles/edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("profileForm") @Valid Profile profile,
                         @RequestParam("file") MultipartFile file, BindingResult bindingResult,
                         SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){
            return "profiles/edit";
        }
        System.out.println(profile.toString());
        if(!file.isEmpty()){
            String fileName = storageService.save(file);
            profile.setPhoto(fileName);
        }
        profile.setUser(authUtils.getCurrentUser().get());
        profileService.save(profile);
        sessionStatus.setComplete();
        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        String fileName = profileService.get(id).getPhoto();
        profileService.delete(id);
        storageService.delete(fileName);
        return "redirect:/profiles";
    }

    /*
    @GetMapping("/search/{page}")
    public String searchForProfiles(@PathVariable("page") Optional<Integer> curPage, Model model){
        int currentPage = curPage.orElse(1);
        Page<Profile> profiles = profileService.listAll(currentPage);
        model.addAttribute("profiles", profiles);
        model.addAttribute("currentPage", currentPage);
        return "profiles/search";
    }
    */

    @GetMapping("/search")
    public String searchProfiles(@PathVariable(value = "page") Optional<Integer> curPage, @ModelAttribute("search") ProfileSearch search, Model model,
                                 BindingResult bindingResult, HttpServletRequest request){
        int currentPage = curPage.orElse(1);
        Page<Profile> profiles = null;
        model.addAttribute("search", search);
        if(search.getState() != null && search.getCraft() != null){
            profiles = profileService.searchByStateAndService(search.getCraft() ,search.getState(), currentPage);
        }else if(search.getCraft() != null){
            profiles = profileService.searchByService(search.getCraft(), currentPage);
        }else{
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "There was an error in the request");
        }
        model.addAttribute("profiles", profiles);
        model.addAttribute("search", search);
        return "profiles/search";
    }


    @GetMapping("/search/{page}")
    public String searchByStateAndService(@PathVariable(value = "page") Optional<Integer> curPage,
                                          @RequestParam(value = "state", required = false, defaultValue = "0") int stateId,
                                          @RequestParam("craft") int craftId, Model model){

        Craft craft = craftService.get(craftId);
        int currentPage = curPage.orElse(1);
        ProfileSearch search = new ProfileSearch();
        Page<Profile> profiles = null;
        if(stateId == 0){ // no state selected
            profiles = profileService.searchByService(craft, currentPage);
        }else {
            State state = stateService.get(stateId);
            profiles = profileService.searchByStateAndService(craft, state, currentPage);
        }

        model.addAttribute("profiles", profiles);
        model.addAttribute("search", search);
        return "profiles/search";
    }


    @GetMapping("/view/{id}")
    public String viewProfile(@PathVariable("id") int id, Model model){
        Profile profile = profileService.get(id);
        int postedJobsCount = jobService.getPostedJobsCount(profile);
        int completedJobsCount = bidService.countCompletedJobs(profile);
        List<JobReview> jobReviews  = jobReviewService.findByBidder(profile);

        int onBudgetPercentage = jobReviewService.getOnBudgetPercentage(jobReviews);
        int onTimePercentage = jobReviewService.getOnTimePercentage(jobReviews);

        model.addAttribute("profile", profile);
        model.addAttribute("postedJobsCount", postedJobsCount);
        model.addAttribute("completedJobsCount", completedJobsCount);

        model.addAttribute("jobReviews", jobReviews);
        model.addAttribute("onBudgetPercentage", onBudgetPercentage);
        model.addAttribute("onTimePercentage", onTimePercentage);
        return "profiles/view";
    }

}
