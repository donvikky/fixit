package com.fixit.web.controller;

import com.fixit.web.entity.Bid;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import com.fixit.web.exceptions.ResourceNotFoundException;
import com.fixit.web.service.BidService;
import com.fixit.web.service.JobService;
import com.fixit.web.service.MessagingService;
import com.fixit.web.service.UserService;
import com.fixit.web.utils.AuthUtils;
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

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/bids")
public class BidController {

    private BidService bidService;
    private JobService jobService;
    private AuthUtils authUtils;

    private MessagingService messagingService;

    private UserService userService;

    @Autowired
    public BidController(BidService bidService, JobService jobService, AuthUtils authUtils,
                         @Qualifier("telegramMessagingService") MessagingService messagingService,
                         UserService  userService) {
        this.bidService = bidService;
        this.jobService = jobService;
        this.authUtils = authUtils;
        this.messagingService = messagingService;
        this.userService = userService;
    }

    //@PreAuthorize(value = "principal.user.profile != null")
    @PreAuthorize("@securityService.hasProfile()")
    @PostMapping("/create")
    public String submitBid(@Valid Bid bid, BindingResult bindingResult, SessionStatus sessionStatus,
                            RedirectAttributes redirectAttributes){

        Profile bidder = authUtils.getCurrentUser().get().getProfile();
//        if(bidder == null){
//            redirectAttributes.addFlashAttribute("errorMessage", "Please update" +
//                    " your profile first before attempting  to  place a bid.");
//            return "redirect:/jobs/" + bid.getJob().getId();
//        }
        List<Bid> bids = bidService.findByJobAndBidder(bid.getJob(), bidder);

        if(!bids.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "You cannot bid for the same job multiple times");
            return "redirect:/jobs/" + bid.getJob().getId();
        }


        bid.setBidder(bidder);
        bid.setAccepted(false);
        Bid savedBid  = bidService.save(bid);

        String messageSubject = "New bid made";
        String messageBody = String.format("An artisan has indicated interest on the %s job you posted. " +
                "Please login to fixit.works to review the bid and contact the artisan", bid.getJob().getCraft().getName());
        messagingService.send(savedBid.getJob().getProfile().getTelegramId(), messageBody);
        redirectAttributes.addFlashAttribute("successMessage", "The bid was submitted successfully");

        return "redirect:/jobs/" + bid.getJob().getId();
    }

    @GetMapping("/job/{id}/{page}")
    public String listJobBids(@PathVariable("id") int id, @PathVariable("page")Optional<Integer> curPage, Model model){
        int currentPage = curPage.orElse(1);

        Job job = jobService.get(id);
        if(job == null){
            throw new ResourceNotFoundException("The job was not found");
        }
        Profile profile = authUtils.getCurrentUser().get().getProfile();
        Page<Bid> bids = bidService.findByJobAndPoster(job, profile, currentPage);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("bids", bids);
        model.addAttribute("job", job);
        return "bids/list";
    }

    @PostMapping("/accept")
    public String acceptBid(@RequestParam("id") Integer id, @RequestParam("jobId") Integer jobId,
                            RedirectAttributes redirectAttributes){


        try {
            bidService.get(id);
            Job job = jobService.get(jobId);
            bidService.acceptBid(id);
            bidService.declineOtherBids(job, id);
            redirectAttributes.addFlashAttribute("successMessage", "The bid was accepted successfully");
            return "redirect:/bids/job/" + jobId  + "/1";
        }catch (NoSuchElementException exception){
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/my")
    public String showMyBids(Model model){
        Profile  profile =  new AuthUtils(userService).getCurrentUser().get().getProfile();
        List<Bid> bids = bidService.findByBidder(profile);
        model.addAttribute("bids", bids);
        return "bids/my";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id, RedirectAttributes redirectAttributes){
        bidService.delete(id);
        redirectAttributes.addFlashAttribute("message", "The bid has been deleted successfully");
        return "redirect:/bids/my";
    }
}
