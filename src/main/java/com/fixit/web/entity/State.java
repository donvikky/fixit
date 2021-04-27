package com.fixit.web.entity;

import com.fixit.web.audit.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "state")
public class State extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min = 3, message = "You must input at least 3 characters")
    private String name;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "state", orphanRemoval = true)
    private List<Lga> lgas = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "state", orphanRemoval = true)
    private List<Town> towns = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "state", orphanRemoval = true)
    private List<Profile> profiles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "state", orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "state", orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();

    public State(){

    }

    public State(@NotBlank @Size(min = 3, message = "You must input at least 3 characters") String name, List<Lga> lgas) {
        this.name = name;
        this.lgas = lgas;
    }

    public State(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Lga> getLgas() {
        return lgas;
    }

    public void setLgas(List<Lga> lgas) {
        this.lgas = lgas;
    }

    public List<Town> getTowns() {
        return towns;
    }

    public void setTowns(List<Town> towns) {
        this.towns = towns;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
