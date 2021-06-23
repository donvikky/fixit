package com.fixit.web.repository;

import com.fixit.web.entity.Bid;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Integer> {
    List<Bid> findByJobAndBidder(Job job, Profile bidder);

    @Query("SELECT b FROM Bid b WHERE b.job = :job AND b.job.profile = :profile")
    Page findByJobAndPoster(@Param("job") Job job, @Param("profile") Profile profile, Pageable pageable);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Bid b set b.accepted = true WHERE b.id = :id")
    int updateAcceptedBid(@Param("id") Integer id);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Bid b set b.accepted = false WHERE b.job = :job AND b.id != :id")
    int updateOtherBidsToDeclined(@Param("job") Job job, @Param("id") Integer id);

    int countByBidderAndAccepted(Profile bidder, Boolean accepted);
}
