package com.fixit.web.controller;

import com.fixit.web.entity.Bid;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import com.fixit.web.service.BidService;
import com.fixit.web.service.JobService;
import com.fixit.web.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/bids")
public class BidController {

    private BidService bidService;
    private JobService jobService;
    private AuthUtils authUtils;

    @Autowired
    public BidController(BidService bidService, JobService jobService, AuthUtils authUtils) {
        this.bidService = bidService;
        this.jobService = jobService;
        this.authUtils = authUtils;
    }

    @PostMapping("/create")
    public String submitBid(@Valid Bid bid, BindingResult bindingResult, SessionStatus sessionStatus,
                            RedirectAttributes redirectAttributes){

        Profile bidder = authUtils.getCurrentUser().get().getProfile();
        List<Bid> bids = bidService.findByJobAndBidder(bid.getJob(), bidder);

        if(!bids.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "You cannot bid for the same job multiple times");
            return "redirect:/jobs/" + bid.getJob().getId();
        }


        bid.setBidder(bidder);
        bid.setAccepted(false);
        bidService.save(bid);
        redirectAttributes.addFlashAttribute("successMessage", "The bid was submitted successfully");

        return "redirect:/jobs/" + bid.getJob().getId();
    }

    @GetMapping("/job/{id}")
    public String listJobBids(@PathVariable("id") int id, Model model){
        Job job = jobService.get(id);
        Profile profile = authUtils.getCurrentUser().get().getProfile();
        List<Bid> bids = bidService.findByJobAndPoster(job, profile);
        model.addAttribute("bids", bids);
        model.addAttribute("job", job);
        return "bids/list";
    }

    @PostMapping("/accept")
    public String acceptBid(@RequestParam("id") Integer id, @RequestParam("jobId") Integer jobId){


        try {
            Bid bid = bidService.get(id);
            Job job = jobService.get(jobId);
            bidService.acceptBid(id);
            bidService.declineOtherBids(job, id);
            return "redirect:/bids/job/" + jobId;
        }catch (NoSuchElementException exception){
            exception.printStackTrace();
            return "redirect:/dashboard";
        }
    }
}
