package com.fixit.web.service;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.State;
import com.fixit.web.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private JobRepository jobRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> listAll() {
        return jobRepository.findAll();
    }

    public Page listAll(final int pageNumber){
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return jobRepository.findAll(pageable);
    }

    public Job save(Job job) {
        return jobRepository.save(job);
    }

    public Job get(int id) {
        return jobRepository.findById(id).get();
    }

    public void delete(int id) {
        jobRepository.deleteById(id);
    }

    public Page findByCreateUser(Profile profile, final int pageNumber){
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return jobRepository.findAllByProfile(profile, pageable);
    }

    public int getPostedJobsCount(Profile profile){
        return jobRepository.countByProfile(profile);
    }

    public List<Job> findMostRecentJobs(){
        return jobRepository.findFirst5ByOrderByIdDesc();
    }

    public Page searchByStateAndService(State state, Craft craft, final int pageNumber){
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return jobRepository.findByStateAndCraft(state, craft, pageable);
    }

}
