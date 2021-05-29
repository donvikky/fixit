package com.fixit.web.entity;

import com.fixit.web.audit.Auditable;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "bids")
public class Bid extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id")
    private Profile bidder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @NotNull
    @Min(value = 1)
    private Double rate;

    @NotNull
    @Min(value = 1, message = "Provided rate must be greater than 0")
    @Column(name = "completion_time")
    private Integer completionTime;

    @NotNull
    @Min(value = 1)
    @Column(name = "completion_time_duration")
    private Integer completionTimeDuration;

    private Boolean accepted;

    public Bid() {
    }

    public Bid(Job job, Profile bidder, Double rate, Integer completionTime, Integer completionTimeDuration,
               Boolean accepted) {
        this.job = job;
        this.bidder = bidder;
        this.rate = rate;
        this.completionTime = completionTime;
        this.completionTimeDuration = completionTimeDuration;
        this.accepted = accepted;
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

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Integer completionTime) {
        this.completionTime = completionTime;
    }

    public Integer getCompletionTimeDuration() {
        return completionTimeDuration;
    }

    public void setCompletionTimeDuration(Integer completionTimeDuration) {
        this.completionTimeDuration = completionTimeDuration;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", job=" + job +
                ", bidder=" + bidder +
                ", rate=" + rate +
                ", completionTime=" + completionTime +
                ", getCompletionTimeDuration=" + completionTimeDuration +
                '}';
    }

}
