package com.fixit.web.entity;

import com.fixit.web.audit.Auditable;

import javax.persistence.*;

@Entity
@Table(name = "job_reviews")
public class JobReview extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id")
    private Profile bidder;

    @Column(name = "completed_on_time")
    private Boolean completedOnTime;

    @Column(name = "completed_on_budget")
    private Boolean completedOnBudget;

    private Integer rating;

    private String comments;

    public JobReview(){

    }

    public JobReview(Job job, Profile bidder, Integer rating, Boolean completedOnTime, Boolean completedOnBudget, String comments) {
        this.job = job;
        this.bidder = bidder;
        this.completedOnTime = completedOnTime;
        this.completedOnBudget = completedOnBudget;
        this.comments = comments;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Profile getBidder() {
        return bidder;
    }

    public void setBidder(Profile bidder) {
        this.bidder = bidder;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getCompletedOnTime() {
        return completedOnTime;
    }

    public void setCompletedOnTime(Boolean completedOnTime) {
        this.completedOnTime = completedOnTime;
    }

    public Boolean getCompletedOnBudget() {
        return completedOnBudget;
    }

    public void setCompletedOnBudget(Boolean completedOnBudget) {
        this.completedOnBudget = completedOnBudget;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }



    @Override
    public String toString() {
        return "JobReview{" +
                "id=" + id +
                ", completedOnTime=" + completedOnTime +
                ", completedOnBudget=" + completedOnBudget +
                ", comments='" + comments + '\'' +
                '}';
    }
}
