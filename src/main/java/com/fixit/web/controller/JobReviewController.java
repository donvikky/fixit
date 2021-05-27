package com.fixit.web.controller;

import com.fixit.web.entity.Job;
import com.fixit.web.entity.JobReview;
import com.fixit.web.entity.Profile;
import com.fixit.web.service.JobReviewService;
import com.fixit.web.service.JobService;
import com.fixit.web.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/job-reviews")
public class JobReviewController {

    private JobReviewService jobReviewService;
    private JobService jobService;
    private AuthUtils authUtils;

    @Autowired
    public JobReviewController(JobReviewService jobReviewService, JobService jobService,
                               AuthUtils authUtils) {
        this.jobReviewService = jobReviewService;
        this.jobService = jobService;
        this.authUtils = authUtils;
    }

    @GetMapping("/create/{id}")
    public String addReview(@PathVariable("id") int id, Model model){
        try {
            Job job = jobService.get(id);
            if(job.getProfile().getId() != authUtils.getCurrentUser().get().getProfile().getId()){
                throw new Exception("profile doesn't match job");
            }
            if(job.getReview() != null){
                throw new Exception("You can only submit one review per job");
            }
            Profile bidder = job.getBids().stream().filter(bid -> bid.getAccepted() == true).findFirst().get().getBidder();
            model.addAttribute("jobReview", new JobReview());
            model.addAttribute("job", job);
            model.addAttribute("bidder", bidder);
            return "jobreviews/create";
        }catch (NoSuchElementException exception){
            return "redirect:/jobs";
        }catch (Exception e){
            return "redirect:/jobs";
        }
    }

    @PostMapping("/create")
    public String saveReview(@Valid JobReview jobReview, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().stream().forEach( error -> System.out.println(error.toString()));
            return "jobreviews/create";
        }
        jobReviewService.save(jobReview);
        return "redirect:/jobs";
    }

    @GetMapping("/edit/{id}")
    public String editReview(@PathVariable("id") int id, Model model){
        JobReview jobReview = jobReviewService.get(id);
        model.addAttribute("jobReview", jobReview);
        return "jobreviews/edit";
    }

    @PostMapping("/edit")
    public String updateReview(@Valid JobReview jobReview, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().stream().forEach( error -> System.out.println(error.toString()));
            return "jobreviews/edit";
        }
        jobReviewService.save(jobReview);
        return "redirect:/jobs";
    }
}
