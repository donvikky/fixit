package com.fixit.web.repository;

import com.fixit.web.entity.JobReview;
import com.fixit.web.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobReviewRepository extends JpaRepository<JobReview, Integer> {
    int countByBidder(Profile profile);
}
