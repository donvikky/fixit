package com.fixit.web.model;

import com.fixit.web.entity.Role;
import com.fixit.web.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class UserRegistration {

    @NotEmpty
    @Size(min = 3)
    private String username;

    @NotEmpty
    @Size(min = 3)
    private String password;

    @NotEmpty
    @Size(min = 3)
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

    public User create(PasswordEncoder passwordEncoder){
        return new User(username, passwordEncoder.encode(password), true, roles);
    }

}
