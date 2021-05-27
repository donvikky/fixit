package com.fixit.web.service;

import com.fixit.web.entity.JobReview;
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

}
