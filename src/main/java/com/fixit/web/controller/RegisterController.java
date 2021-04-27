package com.fixit.web.controller;

import com.fixit.web.entity.Role;
import com.fixit.web.model.UserRegistration;
import com.fixit.web.service.RoleService;
import com.fixit.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showRegistrationForm(Model model){
        UserRegistration user = new UserRegistration();
        List<Role> roles = roleService.listAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "auth/register";
    }

    @PostMapping
    public String processUserRegistration(@Valid @ModelAttribute("user") UserRegistration userRegistration, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "auth/register";
        }
        userService.save(userRegistration.create(passwordEncoder));
        return "redirect:/";
    }
}
