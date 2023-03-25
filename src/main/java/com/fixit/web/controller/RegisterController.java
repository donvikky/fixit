package com.fixit.web.controller;

import com.fixit.web.entity.Role;
import com.fixit.web.entity.User;
import com.fixit.web.model.UserRegistration;
import com.fixit.web.service.MessagingService;
import com.fixit.web.service.RoleService;
import com.fixit.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private MessagingService messagingService;

    @Value("${application.base.url}")
    private String baseUrl;

    @Autowired
    public RegisterController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, MessagingService messagingService) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.messagingService = messagingService;
    }

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

        User newUser = userService.save(userRegistration.create(passwordEncoder));
        String verificationEmailSubject = "Please verify your email address to activate your account";
        String verificationEmailMessage = String.format("Thank you for signing up with Fixit. Please click the link below" +
                "to activate your account.  <br> <a href='%s/verify/user/%s'>Activate your account</a>",
                baseUrl, newUser.getVerificationToken());
        messagingService.send(newUser.getUsername(), verificationEmailSubject, verificationEmailMessage);
        redirectAttributes.addFlashAttribute("message", "Your registration was successful! A verification email has been" +
                " sent to you. Please click the link in the mail to activate your account");
        return "redirect:/login";
    }
}
