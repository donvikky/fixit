package com.fixit.web.controller;

import com.fixit.web.entity.*;
import com.fixit.web.service.*;
import com.fixit.web.utils.AuthUtils;
import com.fixit.web.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private JobService jobService;
    private StateService stateService;
    private CraftService craftService;

    private ProfileService profileService;
    private MessagingService messagingService;
    private AuthUtils authUtils;

    @Autowired
    public JobController(JobService jobService, StateService stateService, CraftService craftService,
                         AuthUtils authUtils, @Qualifier("telegramMessagingService") MessagingService messagingService,
                         ProfileService profileService) {
        this.jobService = jobService;
        this.stateService = stateService;
        this.craftService = craftService;
        this.authUtils = authUtils;
        this.messagingService = messagingService;
        this.profileService = profileService;
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

    @GetMapping("/listing/{page}")
    public String showAllJobs(@PathVariable("page") Optional<Integer> curPage, Model model){
        int currentPage = curPage.orElse(1);
        Page<Job> jobs = jobService.listAll(currentPage);
        model.addAttribute("job", new Job());
        model.addAttribute("jobs", jobs);
        return "jobs/all";
    }

    @PostMapping("/listing/{page}")
    public String searchJobs(@RequestParam("state") int stateId, @RequestParam("craft") int craftId, Model model){
        State state = stateService.get(stateId);
        Craft craft = craftService.get(craftId);
        int startPage = 1;
        Page<Job> jobs = jobService.searchByStateAndService(state, craft, startPage);
        model.addAttribute("jobs", jobs);
        return "jobs/all";
    }

    @GetMapping("/page/{page}")
    public String listJobs(@PathVariable("page") Optional<Integer> curPage, Model model){
        int currentPage = curPage.orElse(1);
        Page<Job> jobs = jobService.findByCreateUser(authUtils.getCurrentUser().get().getProfile(), currentPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("jobs", jobs);
        return "jobs/list";
    }

    //@PreAuthorize(value = "principal.user.profile != null")
    @PreAuthorize("@securityService.hasProfile()")
    @GetMapping("/create")
    public String createJob(Model model){
        List<State> states = stateService.listAll();
        List<Craft> crafts = craftService.listAll();

        model.addAttribute("job", new Job());
        return "jobs/create";
    }

    @PreAuthorize(value = "principal.user.profile != null")
    @PostMapping("/create")
    public String saveJob(@Valid Job job, BindingResult bindingResult, SessionStatus sessionStatus, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            //bindingResult.getAllErrors().stream().forEach(e -> System.out.println(e.toString()));
            return "jobs/create";
        }
        job.setProfile(authUtils.getCurrentUser().get().getProfile());
        Job savedJob = jobService.save(job);

        //get all profiles that have requested notification and send notification.
        //1. set telegram message
        String message = String.format("A new %s job has been posted. You can access the job here https://%s/jobs/%s to" +
                " review the job details and notify the poster of your interest.", savedJob.getCraft().getName(),
                request.getServerName(), savedJob.getId());

        List<Profile> profiles = profileService.getProfilesForNotificationByStateAndCraft(savedJob.getCraft().getId(),
                savedJob.getState(), savedJob.getProfile().getId());


        List<String> telegramIds = new ArrayList<>();
        profiles.stream().filter(profile -> !profile.getTelegramId().isEmpty()).forEach(profile -> telegramIds.add(profile.getTelegramId()));

        if(telegramIds.size() > 0){
            messagingService.sendAll(telegramIds, message);
        }
        
        sessionStatus.setComplete();
        return "redirect:/jobs/page/1";
    }


    @PreAuthorize(value = "principal.user.profile != null")
    @GetMapping("/edit/{id}")
    public String editJob(@PathVariable("id") int id, Model model){
        Job job = jobService.get(id);
        model.addAttribute("job", job);
        return "jobs/edit";
    }


    @PreAuthorize(value = "principal.user.profile != null")
    @PostMapping("/edit")
    public String updateJob(@Valid Job job, BindingResult bindingResult, SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){
            return "jobs/edit";
        }
        job.setProfile(authUtils.getCurrentUser().get().getProfile());
        jobService.save(job);
        sessionStatus.setComplete();
        return "redirect:/jobs";
    }

    @GetMapping("/{id}")
    public String viewJob(@PathVariable("id") int id, Model model){
        Job job = jobService.get(id);
        String postDuration = new DateUtils().getTimeIntervalDisplayText(Timestamp.valueOf(job.getCreatedAt()));

        model.addAttribute("job", job);
        model.addAttribute("bid", new Bid());
        model.addAttribute("postDuration", postDuration);
        return "jobs/view";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("authentication.principal.user.profile != null")
    public String delete(@PathVariable("id") int id){
        jobService.delete(id);
        return "redirect:/jobs";
    }

    @PostMapping("/complete")
    public String completeJob(@RequestParam("id") int id, RedirectAttributes redirectAttributes){
        try {
            Job job = jobService.get(id);
            job.setCompleted(true);
            jobService.save(job);
            redirectAttributes.addFlashAttribute("successMessage", "The job has been marked  as completed");
        }catch (NoSuchElementException exception){
            exception.printStackTrace();
        }
        return "redirect:/jobs/page/" + 1;
    }
}
