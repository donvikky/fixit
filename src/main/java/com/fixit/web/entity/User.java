package com.fixit.web.entity;

import com.fixit.web.enums.Provider;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    private Boolean enabled;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "verification_token_expiry")
    private LocalDateTime verificationTokenExpiry;

    public User() {
    }

    public User(String username, String password, Boolean enabled, List<Role> roles, String verificationToken, LocalDateTime verificationTokenExpiry) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.verificationToken = verificationToken;
        this.verificationTokenExpiry =  verificationTokenExpiry;
    }

    public User(String username, String password, Boolean enabled, String verificationToken, LocalDateTime verificationTokenExpiry) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.verificationToken = verificationToken;
        this.verificationTokenExpiry =  verificationTokenExpiry;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public LocalDateTime getVerificationTokenExpiry() {
        return verificationTokenExpiry;
    }

    public void setVerificationTokenExpiry(LocalDateTime verificationTokenExpiry) {
        this.verificationTokenExpiry = verificationTokenExpiry;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", enabled=" + enabled +
                ", provider=" + provider +
                ", providerId=" + providerId +
                '}';
    }

}
