package com.fixit.web.service;

import com.fixit.web.entity.ProjectPhoto;
import com.fixit.web.repository.ProjectPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectPhotoService {

    @Autowired
    private ProjectPhotoRepository projectPhotoRepository;

    public List<ProjectPhoto> listAll() {
        return projectPhotoRepository.findAll();
    }

    public void save(ProjectPhoto photos) {
        projectPhotoRepository.save(photos);
    }

    public ProjectPhoto get(int id) {
        return (ProjectPhoto) projectPhotoRepository.findById(id).get();
    }

    public void delete(int id) {
        projectPhotoRepository.deleteById(id);
    }
}
