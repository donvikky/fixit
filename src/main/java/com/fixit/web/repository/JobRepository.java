package com.fixit.web.repository;

import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {
    Page findAllByProfile(Profile profile, Pageable pageable);
}
