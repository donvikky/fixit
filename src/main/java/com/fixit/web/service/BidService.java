package com.fixit.web.service;

import com.fixit.web.entity.Bid;
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
}
