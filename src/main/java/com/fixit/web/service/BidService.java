package com.fixit.web.service;

import com.fixit.web.entity.Bid;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import com.fixit.web.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidService {

    private BidRepository bidRepository;

    @Autowired
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public List<Bid> listAll() {
        return bidRepository.findAll();
    }

    public void save(Bid bid) {
        bidRepository.save(bid);
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

    public List<Bid> findByJobAndPoster(Job job, Profile poster){
        return bidRepository.findByJobAndPoster(job, poster);
    }

    public int acceptBid(Integer id){
        System.out.println("Updating bid with ID: " + id);
        return bidRepository.updateAcceptedBid(id);
    }

    public int declineOtherBids(Job job, Integer id){
        System.out.println("Declining other bids aside from ID: " + id);
        return bidRepository.updateOtherBidsToDeclined(job, id);
    }

}
