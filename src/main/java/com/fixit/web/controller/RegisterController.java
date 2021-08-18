package com.fixit.web.controller;

import com.fixit.web.entity.Role;
import com.fixit.web.entity.User;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

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
    public String processUserRegistration(@Valid @ModelAttribute("user") UserRegistration userRegistration,
                                          BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        if(bindingResult.hasErrors()){
            return "auth/register";
        }
        //check if username is taken
        User user = userService.findByUsername(userRegistration.getUsername().toLowerCase(Locale.ROOT));
        if(user != null){
            model.addAttribute("message", "That username is not available. Please choose another username");
            return "auth/register";
        }
        userService.save(userRegistration.create(passwordEncoder));
        redirectAttributes.addFlashAttribute("message", "Your registration was successful! Please log in");
        return "redirect:/login";
    }
}
