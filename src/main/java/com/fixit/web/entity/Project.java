package com.fixit.web.entity;

import com.fixit.web.audit.Auditable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "short_description")
    @NotBlank
    private String shortDescription;

    @Column(name = "long_description")
    @NotBlank
    private String longDescription;

    @Column(name = "budget_minimum")
    @Min(value = 0)
    private Double budgetMinimum;

    @Column(name = "budget_maximum")
    @Min(value = 0)
    private Double budgetMaximum;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    @NotNull
    private State state;

    @Transient
    private MultipartFile[] files;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project", orphanRemoval = true)
    private List<ProjectPhoto> projectPhotos = new ArrayList<>();

    public Project() {
    }

    public Project(Profile profile, String shortDescription, String longDescription, Double budgetMinimum, Double budgetMaximum, Date date, State state, MultipartFile[] files) {
        this.profile = profile;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.budgetMinimum = budgetMinimum;
        this.budgetMaximum = budgetMaximum;
        this.date = date;
        this.state = state;
        this.files = files;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Double getBudgetMinimum() {
        return budgetMinimum;
    }

    public void setBudgetMinimum(Double budgetMinimum) {
        this.budgetMinimum = budgetMinimum;
    }

    public Double getBudgetMaximum() {
        return budgetMaximum;
    }

    public void setBudgetMaximum(Double budgetMaximum) {
        this.budgetMaximum = budgetMaximum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public List<ProjectPhoto> getProjectPhotos() {
        return projectPhotos;
    }

    public void setProjectPhotos(List<ProjectPhoto> projectPhotos) {
        this.projectPhotos = projectPhotos;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", profile=" + profile +
                ", shortDescription='" + shortDescription + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", budgetMinimum=" + budgetMinimum +
                ", budgetMaximum=" + budgetMaximum +
                ", date=" + date +
                '}';
    }
}
