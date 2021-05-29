package com.fixit.web.repository;

import com.fixit.web.entity.Profile;
import com.fixit.web.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Page findByProfile(Profile profile, Pageable pageable);
}
