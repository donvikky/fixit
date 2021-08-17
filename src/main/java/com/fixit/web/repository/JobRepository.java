package com.fixit.web.repository;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Job;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {
    Page findAllByProfile(Profile profile, Pageable pageable);
    Page findAll(Pageable pageable);
    Page findByStateAndCraft(State state, Craft craft, Pageable pageable);
    int countByProfile(Profile profile);
    List<Job> findFirst5ByOrderByIdDesc();
}
