package com.fixit.web.repository;

import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findAllByProfile(Profile profile);
}
