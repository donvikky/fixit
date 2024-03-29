package com.fixit.web.entity;

import com.fixit.web.audit.Auditable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Please provide a valid email")
    private String email;

    @Column(name = "first_name")
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    private String lastName;

    @Column(name = "mobile_number", nullable = true)
    @NotBlank(message = "Please provide your mobile number")
    @Size(min=10, max=20)
    private String mobileNumber;

    private String address;
    private String photo;

    @Column(name = "hourly_rate")
    @Digits(integer = 10, fraction = 2)
    private Integer hourlyRate;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(columnDefinition = "TEXT", name = "long_description")
    private String longDescription;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private State state;

    @Column(name = "receive_job_notification")
    private Boolean  receiveJobNotification;

    @Transient
    private MultipartFile file;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "profiles_services", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<Craft> crafts;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "profile", orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "profile", orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "bidder", orphanRemoval = true)
    private List<JobReview> jobReviews = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "bidder", orphanRemoval = true)
    private List<Bid> bids = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "bookmarker", orphanRemoval = true)
    private List<BookMark> bookMarks = new ArrayList<>();

    @Transient
    private Double rating;

    @Column(name = "telegram_id")
    private String telegramId;
    private int views;
    public Profile() {

    }

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public Profile(String email, String firstName, String lastName, String mobileNumber, String address, String photo,
                   User user, State state, MultipartFile file, List<Craft> crafts, Integer hourlyRate,
                   String shortDescription, String longDescription, Boolean  receiveJobNotification) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.photo = photo;
        this.user = user;
        this.state = state;
        this.file = file;
        this.crafts = crafts;
        this.hourlyRate = hourlyRate;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.receiveJobNotification  = receiveJobNotification;
    }

    public Profile(String email, String firstName, String lastName, String mobileNumber, String address,  State state) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.state = state;
    }

    public Profile(Integer id, String email, String firstName, String lastName, String mobileNumber, String address,
                   State state, String photo){
        this(email, firstName, lastName, mobileNumber, address, state);
        this.id = id;
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<Craft> getCrafts() {
        return crafts;
    }

    public void setCrafts(List<Craft> crafts) {
        this.crafts = crafts;
    }

    public Integer getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Integer hourlyRate) {
        this.hourlyRate = hourlyRate;
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

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public List<JobReview> getJobReviews() {
        return jobReviews;
    }

    public void setJobReviews(List<JobReview> jobReviews) {
        this.jobReviews = jobReviews;
    }

    public List<BookMark> getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(List<BookMark> bookMarks) {
        this.bookMarks = bookMarks;
    }

    public Boolean getReceiveJobNotification() {
        return receiveJobNotification;
    }

    public void setReceiveJobNotification(Boolean receiveJobNotification) {
        this.receiveJobNotification = receiveJobNotification;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Double getRating(){
        Double totalRatings  = 0D;
        for(JobReview review : getJobReviews()){
            totalRatings += review.getRating();
        }
        return totalRatings >  0 ? totalRatings / getJobReviews().size() : 0D;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", address='" + address + '\'' +
                ", photo='" + photo + '\'' +
                ", user=" + user +
                ", state=" + state +
                ", receiveNotifications=" + receiveJobNotification +
                '}';
    }
}
