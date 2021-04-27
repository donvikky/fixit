package com.fixit.web.repository;

import com.fixit.web.entity.Profile;
import com.fixit.web.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByProfile(Profile profile);
}
