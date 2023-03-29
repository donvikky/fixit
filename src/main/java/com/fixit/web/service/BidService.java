package com.fixit.web.service;

import com.fixit.web.entity.Bid;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import com.fixit.web.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidService {

    private BidRepository bidRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    @Autowired
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public List<Bid> listAll() {
        return bidRepository.findAll();
    }

    public Bid save(Bid bid) {
        return bidRepository.save(bid);
    }

    public Bid get(int id) {
        return (Bid) bidRepository.findById(id).get();
    }

    public void delete(int id) {
        bidRepository.deleteById(id);
    }

    public List<Bid> findByJobAndBidder(Job job, Profile bidder){
        return bidRepository.findByJobAndBidder(job, bidder);
    }

    public Page findByJobAndPoster(Job job, Profile poster, final int pageNumber){
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return bidRepository.findByJobAndPoster(job, poster, pageable);
    }

    public int acceptBid(Integer id){
        System.out.println("Updating bid with ID: " + id);
        return bidRepository.updateAcceptedBid(id);
    }

    public int declineOtherBids(Job job, Integer id){
        System.out.println("Declining other bids aside from ID: " + id);
        return bidRepository.updateOtherBidsToDeclined(job, id);
    }

    public int getBidsWon(Profile bidder){
        return bidRepository.countByBidderAndAccepted(bidder, true);
    }


}
