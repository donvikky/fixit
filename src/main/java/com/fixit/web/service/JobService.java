package com.fixit.web.service;

import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import com.fixit.web.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> listAll() {
        return jobRepository.findAll();
    }

    public void save(Job job) {
        jobRepository.save(job);
    }

    public Job get(int id) {
        return jobRepository.findById(id).get();
    }

    public void delete(int id) {
        jobRepository.deleteById(id);
    }

    public List<Job> findByCreateUser(Profile profile){
        return jobRepository.findAllByProfile(profile);
    }

}
