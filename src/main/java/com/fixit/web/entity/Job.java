package com.fixit.web.entity;

import com.fixit.web.audit.Auditable;
import com.fixit.web.utils.DateUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private Profile profile;

    @Column(name = "short_description")
    @NotBlank(message = "Please provide a description")
    private String shortDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "craft_id")
    @NotNull
    private Craft craft;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    @NotNull
    private State state;

    @Column(name = "budget_minimum")
    @NotNull
    @Min(value = 0)
    private Double budgetMinimum;

    @Column(name = "budget_maximum")
    @NotNull
    @Min(value = 0)
    private Double budgetMaximum;

    @Column(name = "job_type")
    @NotNull
    private Integer jobType;

    @Column(name = "long_description")
    @NotBlank(message = "Please provide a description")
    private String longDescription;

    private Boolean completed = false;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "job", orphanRemoval = true)
    private List<Bid> bids = new ArrayList<>();

    @OneToOne(mappedBy = "job")
    private JobReview review;

    @Transient
    private String postDuration;

    public Job() {
    }

    public Job(String shortDescription, Craft craft, State state, Double budgetMinimum, Double budgetMaximum, Integer jobType, String longDescription) {
        this.shortDescription = shortDescription;
        this.craft = craft;
        this.state = state;
        this.budgetMinimum = budgetMinimum;
        this.budgetMaximum = budgetMaximum;
        this.jobType = jobType;
        this.longDescription = longDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Craft getCraft() {
        return craft;
    }

    public void setCraft(Craft craft) {
        this.craft = craft;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public JobReview getReview() {
        return review;
    }

    public void setReview(JobReview review) {
        this.review = review;
    }

    public String getPostDuration() {
        postDuration = new DateUtils().getTimeIntervalDisplayText(Timestamp.valueOf(this.getCreatedAt()));
        return postDuration;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", craft=" + craft +
                ", state=" + state +
                ", budgetMinimum=" + budgetMinimum +
                ", budgetMaximum=" + budgetMaximum +
                ", jobType=" + jobType +
                ", longDescription='" + longDescription + '\'' +
                ", completed='" + completed + '\'' +
                '}';
    }
}
