package com.fixit.web.model;

import com.fixit.web.entity.Role;
import com.fixit.web.entity.User;
import com.fixit.web.validator.PasswordEqualField;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@PasswordEqualField(
        passwordField = "password",
        passwordRepeatField = "confirm",
        message = "Provided passwords must be equal all the time"
)
public class UserRegistration {

    @NotEmpty
    @Size(min = 4, message = "Username must contain at least four characters")
    private String username;

    @NotEmpty
    @Size(min = 4, message = "Password must contain at least four characters")
    private String password;

    @NotEmpty
    @Size(min = 4, message = "Repeat password must contain at least four characters")
    private String confirm;

    private List<Role> roles;

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

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String generateVerificationToken() {
        int tokenLength = 30;
        return RandomStringUtils.randomAlphanumeric(tokenLength);
    }

    public LocalDateTime generateVerificationTokenExpiryDate() {
        int tokenExpiryDays = 1;
        return LocalDateTime.now().plusDays(tokenExpiryDays);
    }

    public User create(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), false, roles, generateVerificationToken(),
                generateVerificationTokenExpiryDate());
    }

}
