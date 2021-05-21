package com.fixit.web.controller;

import com.fixit.web.entity.Bid;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import com.fixit.web.service.BidService;
import com.fixit.web.service.JobService;
import com.fixit.web.utils.AuthUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

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
    @PreAuthorize("authentication.principal.user.profile != null")
    public String submitBid(@Valid Bid bid, BindingResult bindingResult, SessionStatus sessionStatus,
                            RedirectAttributes redirectAttributes){

        Profile bidder = new AuthUtils().getCurrentUser().get().getProfile();
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
    @PreAuthorize("authentication.principal.user.profile != null")
    public String listJobBids(@PathVariable("id") int id, Model model){
        Job job = jobService.get(id);
        Profile profile = new AuthUtils().getCurrentUser().get().getProfile();
        List<Bid> bids = bidService.findByJobAndPoster(job, profile);
        model.addAttribute("bids", bids);
        model.addAttribute("job", job);
        return "bids/list";
    }
}
