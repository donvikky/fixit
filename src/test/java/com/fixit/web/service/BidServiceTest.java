package com.fixit.web.service;

import com.fixit.web.entity.Bid;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import com.fixit.web.repository.BidRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BidServiceTest {

    private AutoCloseable autoCloseable;

    private BidService bidService;

    @Mock
    private BidRepository bidRepository;

    private Bid bid;

    private Profile bidder;

    private Job job;

    private int id;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        bidService = new BidService(bidRepository);
        bid = new Bid();
        job = new Job();
        bidder = new Profile();
        id = 1;
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void listAll() {
        bidService.listAll();
        verify(bidRepository).findAll();
    }

    @Test
    void save() {
        bidService.save(bid);
        verify(bidRepository).save(bid);
    }

    @Test
    void get() {
        bid.setId(id);
        when(bidRepository.findById(id)).thenReturn(Optional.of(bid));

        bidService.get(id);
        verify(bidRepository).findById(id);
    }

    @Test
    void delete() {
        bidService.delete(id);
        verify(bidRepository).deleteById(id);

    }

    @Test
    void findByJobAndBidder() {
        bidService.findByJobAndBidder(job, bidder);
        verify(bidRepository).findByJobAndBidder(job, bidder);
    }

    @Test
    @Disabled
    void findByJobAndPoster() {
    }

    @Test
    void acceptBid() {
        int id = 1;
        bidService.acceptBid(id);
        verify(bidRepository).updateAcceptedBid(id);
    }

    @Test
    void declineOtherBids() {
        bidService.declineOtherBids(job, id);
        verify(bidRepository).updateOtherBidsToDeclined(job, id);
    }

    @Test
    void getBidsWon() {
        Profile bidder = new Profile();
        bidService.getBidsWon(bidder);
        verify(bidRepository).countByBidderAndAccepted(bidder, true);
    }

    @Test
    void findByBidder() {
        Profile bidder = new Profile();
        bidService.findByBidder(bidder);
        verify(bidRepository).findByBidder(bidder);
    }

    @Test
    void countCompletedJobs() {
        bidService.countCompletedJobs(bidder);
        verify(bidRepository).findCompletedJobs(bidder);
    }
}