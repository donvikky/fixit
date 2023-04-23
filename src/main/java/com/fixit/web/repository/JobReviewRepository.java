package com.fixit.web.repository;

import com.fixit.web.entity.JobReview;
import com.fixit.web.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobReviewRepository extends JpaRepository<JobReview, Integer> {
    int countByBidder(Profile profile);

    public List<JobReview> findByBidder(Profile profile);
}
