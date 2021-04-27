package com.fixit.web.model;

import com.fixit.web.entity.Profile;
import com.fixit.web.entity.State;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProfileForm {

    private Integer id;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Please provide a valid email")
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String address;
    private MultipartFile photo;
    private String photoName;

    @NotNull
    private State state;

    public ProfileForm() {
    }

    public ProfileForm(String email, String firstName, String lastName, String mobileNumber, String address, MultipartFile photo, State state) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.photo = photo;
        this.state = state;
    }

    public ProfileForm(Profile profile){
        this.id = profile.getId();
        this.email = profile.getEmail();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.mobileNumber = profile.getMobileNumber();
        this.address = profile.getAddress();
        this.state = profile.getState();
        this.photoName = profile.getPhoto();
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

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    @Override
    public String toString() {
        return "ProfileForm{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", address='" + address + '\'' +
                ", photo=" + photo +
                ", state=" + state +
                '}';
    }

    public Profile toProfile(){
        return new Profile(email, firstName, lastName, mobileNumber, address, state);
    }

    public Profile toProfile(Integer id){
        return new Profile(id, email, firstName, lastName, mobileNumber, address, state, photoName);
    }

}
