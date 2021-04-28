package com.fixit.web.controller;

import com.fixit.web.entity.Bid;
import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.State;
import com.fixit.web.service.CraftService;
import com.fixit.web.service.JobService;
import com.fixit.web.service.StateService;
import com.fixit.web.utils.AuthUtils;
import com.fixit.web.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private JobService jobService;
    private StateService stateService;
    private CraftService craftService;

    @Autowired
    public JobController(JobService jobService, StateService stateService, CraftService craftService) {
        this.jobService = jobService;
        this.stateService = stateService;
        this.craftService = craftService;
    }

    @GetMapping
    public String listJobs(Model model){
        List<Job> jobs = jobService.findByCreateUser(new AuthUtils().getCurrentUser().getProfile());
        model.addAttribute("jobs", jobs);
        return "jobs/list";
    }

    @GetMapping("/create")
    @PreAuthorize("authentication.principal.user.profile != null")
    public String createJob(Model model){
        List<State> states = stateService.listAll();
        List<Craft> crafts = craftService.listAll();

        model.addAttribute("job", new Job());
        model.addAttribute("states", states);
        model.addAttribute("crafts", crafts);
        return "jobs/create";
    }

    @PostMapping("/create")
    @PreAuthorize("authentication.principal.user.profile != null")
    public String saveJob(@Valid Job job, BindingResult bindingResult, SessionStatus sessionStatus) {
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().stream().forEach(e -> System.out.println(e.toString()));
            return "jobs/create";
        }
        job.setProfile(new AuthUtils().getCurrentUser().getProfile());
        jobService.save(job);
        sessionStatus.setComplete();
        return "redirect:/jobs";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("authentication.principal.user.profile != null")
    public String editJob(@PathVariable("id") int id, Model model){
        List<State> states = stateService.listAll();
        List<Craft> crafts = craftService.listAll();
        Job job = jobService.get(id);
        model.addAttribute("job", job);
        model.addAttribute("states", states);
        model.addAttribute("crafts", crafts);
        return "jobs/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("authentication.principal.user.profile != null")
    public String updateJob(@Valid Job job, BindingResult bindingResult, SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){
            return "jobs/edit";
        }
        job.setProfile(new AuthUtils().getCurrentUser().getProfile());
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
}
