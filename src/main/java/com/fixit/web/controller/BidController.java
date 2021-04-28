package com.fixit.web.controller;

import com.fixit.web.entity.Bid;
import com.fixit.web.service.BidService;
import com.fixit.web.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/bids")
public class BidController {

    private BidService bidService;
    private JobService jobService;

    @Autowired
    public BidController(BidService bidService, JobService jobService) {
        this.bidService = bidService;
        this.jobService = jobService;
    }

    @PostMapping("/create")
    public String submitBid(@Valid Bid bid, BindingResult bindingResult,
                            SessionStatus sessionStatus, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "redirect:/jobs";
        bidService.save(bid);
        redirectAttributes.addFlashAttribute("success", "The bid was submitted successfully");
        return "redirect:/jobs";
    }
}
