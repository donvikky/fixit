package com.fixit.web.service;

import com.fixit.web.entity.JobReview;
import com.fixit.web.entity.Profile;
import com.fixit.web.repository.JobReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobReviewService {

    private JobReviewRepository jobReviewRepository;

    @Autowired
    public JobReviewService(JobReviewRepository jobReviewRepository) {
        this.jobReviewRepository = jobReviewRepository;
    }

    public List<JobReview> listAll() {
        return jobReviewRepository.findAll();
    }

    public void save(JobReview jobReview) {
        jobReviewRepository.save(jobReview);
    }

    public JobReview get(int id) {
        return jobReviewRepository.findById(id).get();
    }

    public void delete(int id) {
        jobReviewRepository.deleteById(id);
    }

    public int findJobReviewsCount(Profile profile){
        return jobReviewRepository.countByBidder(profile);
    }

    public List<JobReview> findByBidder(Profile profile){
        return jobReviewRepository.findByBidder(profile);
    }

    public int getOnTimePercentage(List<JobReview>  jobReviews){
        if(jobReviews.size() == 0){
            return 0;
        }
        int completedCount = (int) jobReviews.stream().filter(jobReview -> jobReview.getCompletedOnTime() == true).count();
        return Math.round((completedCount / jobReviews.size()) * 100);
    }

    public int getOnBudgetPercentage(List<JobReview>  jobReviews){
        if(jobReviews.size() == 0){
            return 0;
        }
        int budgetCount = (int) jobReviews.stream().filter(jobReview -> jobReview.getCompletedOnBudget() == true).count();
        return Math.round((budgetCount / jobReviews.size())  * 100);
    }

}
