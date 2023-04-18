package com.fixit.web.controller;

import com.fixit.web.entity.*;
import com.fixit.web.repository.UserRepository;
import com.fixit.web.service.BidService;
import com.fixit.web.service.JobService;
import com.fixit.web.service.MessagingService;
import com.fixit.web.service.UserService;
import com.fixit.web.utils.AuthUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration

public class BidControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Authentication auth;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private BidService bidService;
    @MockBean
    private JobService jobService;
    @MockBean
    private AuthUtils authUtils;
    @MockBean
    @Qualifier("telegramMessagingService")
    private MessagingService messagingService;
    @MockBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private Job job;
    private Profile bidder;
    private Bid bid;
    private User user;

    private Craft craft;
    @BeforeEach
    void setUp() {
        craft = new Craft();
        craft.setId(1);
        craft.setName("Plumbing");

        job = new Job();
        job.setId(1);
        job.setCraft(craft);

        bidder = new Profile();
        bidder.setId(1);

        bid = new Bid();
        bid.setJob(job);
        bid.setBidder(bidder);
        bid.setRate(1000.0);
        bid.setCompletionTime(10);
        bid.setCompletionTimeDuration(5);
        bid.setAccepted(false);

        user = new User();
        user.setUsername("test@test.com");
        user.setPassword("1234");
        user.setEnabled(true);
        user.setProfile(bidder);
        //userRepository.save(user);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @Disabled
    @WithMockUser(username = "test@test.com")
    void itShouldCreateNewBid() throws Exception{
        // Mock getCurrentUser to return a user with a profile
//        User user = new User();
//        user.setUsername("test@test.com");
//        Profile profile = new Profile();
//        profile.setId(1);
//        user.setProfile(profile);
        Bid savedBid = bidService.save(bid);

        when(authUtils.getCurrentUser()).thenReturn(Optional.of(user));
        when(bidService.findByJobAndBidder(job, bidder)).thenReturn(List.of());
        when(bidService.save(bid)).thenReturn(savedBid);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("job", bid.getJob().getId().toString());
        params.add("bidder", bidder.getId().toString());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bids/create")
                        .params(params)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/jobs/" + bid.getJob().getId()))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", "The bid was submitted successfully"));
    }

}
