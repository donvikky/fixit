package com.fixit.web.model;

public class ContactProfile {

    private int id;

    private String name;
    private String mobileNumber;

    private String message;

    public ContactProfile(int id, String name, String mobileNumber, String message) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.message = message;
        this.id = id;
    }

    public ContactProfile(){

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ContactProfile{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
