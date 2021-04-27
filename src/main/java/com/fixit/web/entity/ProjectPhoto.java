package com.fixit.web.entity;

import com.fixit.web.audit.Auditable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_photos")
public class ProjectPhoto extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String photo;

    public ProjectPhoto() {
    }

    public ProjectPhoto(Project project, String photo) {
        this.project = project;
        this.photo = photo;
    }

    public ProjectPhoto(Project project){
        this.project = project;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<ProjectPhoto> addPhotos(List<String> fileNames){
        List<ProjectPhoto> photos = new ArrayList<>();
        for (String fileName: fileNames){
            photos.add(new ProjectPhoto(this.project, fileName));
        }
        return photos;
    };

    @Override
    public String toString() {
        return "ProjectPhotos{" +
                "id=" + id +
                ", project=" + project +
                ", photo='" + photo + '\'' +
                '}';
    }
}
