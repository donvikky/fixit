package com.fixit.web.service;

import com.fixit.web.entity.Profile;
import com.fixit.web.entity.Project;
import com.fixit.web.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    public List<Project> listAll() {
        return projectRepository.findAll();
    }

    public void save(Project project) {
        projectRepository.save(project);
    }

    public Project get(int id) {
        return projectRepository.findById(id).get();
    }

    public void delete(int id) {
        projectRepository.deleteById(id);
    }

    public List<Project> findByProfile(Profile profile){
        return projectRepository.findByProfile(profile);
    }

}
