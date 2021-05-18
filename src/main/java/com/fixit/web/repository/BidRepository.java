package com.fixit.web.repository;

import com.fixit.web.entity.Bid;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Integer> {
    List<Bid> findByJobAndBidder(Job job, Profile bidder);

    @Query("SELECT b FROM Bid b WHERE b.job = :job AND b.job.profile = :profile")
    List<Bid> findByJobAndPoster(@Param("job") Job job, @Param("profile") Profile profile);
}
