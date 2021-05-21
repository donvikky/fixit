package com.fixit.web.controller;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.State;
import com.fixit.web.entity.User;
import com.fixit.web.model.ProfileSearch;
import com.fixit.web.service.CraftService;
import com.fixit.web.service.FileStorageService;
import com.fixit.web.service.ProfileService;
import com.fixit.web.service.StateService;
import com.fixit.web.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/profiles")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private ProfileService profileService;
    private StateService stateService;
    private FileStorageService storageService;
    private CraftService craftService;
    private AuthUtils authUtils;

    @Autowired
    public ProfileController(ProfileService profileService, StateService stateService,
                             FileStorageService storageService, CraftService craftService,
                             AuthUtils authUtils) {
        this.profileService = profileService;
        this.stateService = stateService;
        this.storageService = storageService;
        this.craftService = craftService;
        this.authUtils = authUtils;
    }

    @GetMapping
    public String index(Model model){
        List<Profile> profiles = profileService.listAll();
        model.addAttribute("profiles", profiles);
        return "profiles/index";
    }

    @GetMapping("/create")
    public String add(Model model){
        User user = new AuthUtils().getCurrentUser().get();
        logger.info("%s", user);
        Profile profile = profileService.findByUser(authUtils.getCurrentUser().get());
        if(profile != null){
            return "redirect:/profiles/edit";
        }
        profile = new Profile();

        List<State> states = stateService.listAll();
        List<Craft> crafts = craftService.listAll();

        model.addAttribute("profile", profile);
        model.addAttribute("states", states);
        model.addAttribute("crafts", crafts);
        return "profiles/create";
    }

    @PostMapping("/create")
    public String save(@Valid Profile profile, BindingResult bindingResult,
                       @RequestParam("file") MultipartFile file,
                       SessionStatus sessionStatus){
        if(bindingResult.hasErrors() || file.isEmpty()){
            return "profiles/create";
        }
        String fileName = storageService.save(file);
        
        profile.setUser(authUtils.getCurrentUser().get());
        profile.setPhoto(fileName);
        profileService.save(profile);
        sessionStatus.setComplete();
        return "redirect:/dashboard";
    }

    @GetMapping("/edit")
    public String edit(Model model){

        Profile profile = profileService.findByUser(authUtils.getCurrentUser().get());
        logger.info("%s", new AuthUtils().getCurrentUser().get());
        if(profile == null){
            return "redirect:/profiles/create";
        }
        model.addAttribute("profile", profile);

        List<State> states = stateService.listAll();
        List<Craft> crafts = craftService.listAll();

        model.addAttribute("states", states);
        model.addAttribute("crafts", crafts);

        return "profiles/edit";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        Profile profile = profileService.get(id);

        if(profile == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        List<State> states = stateService.listAll();
        List<Craft> crafts = craftService.listAll();
        model.addAttribute("profile", profile);
        model.addAttribute("states", states);
        model.addAttribute("crafts", crafts);

        return "profiles/edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("profileForm") @Valid Profile profile,
                         @RequestParam("file") MultipartFile file, BindingResult bindingResult,
                         SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){
            return "profiles/edit";
        }

        if(!file.isEmpty()){
            String fileName = storageService.save(file);
            profile.setPhoto(fileName);
        }
        profile.setUser(authUtils.getCurrentUser().get());
        profileService.save(profile);
        sessionStatus.setComplete();
        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        String fileName = profileService.get(id).getPhoto();
        profileService.delete(id);
        storageService.delete(fileName);
        return "redirect:/profiles";
    }

    @GetMapping("/search")
    public String searchForProfiles(@RequestParam("craft") Integer craftId, Model model){
        Craft craft = null;
        List<Profile> profiles = new ArrayList<>();
        try {
            craft = craftService.get(craftId);
            profiles = profileService.findByCraft(craft);
        }catch (NoSuchElementException ex){
            //ex.printStackTrace();
            System.out.println("Craft not found");
            System.out.println(profiles.size());
        }
        List<State> states = stateService.listAll();
        List<Craft> crafts = craftService.listAll();
        model.addAttribute("profiles", profiles);
        model.addAttribute("states", states);
        model.addAttribute("crafts", crafts);
        return "profiles/search";
    }

    @PostMapping("/search")
    public String searchProfiles(@ModelAttribute("search") ProfileSearch search, Model model,
                                 BindingResult bindingResult){
        List<Profile> profiles = profileService.findByCraft(search.getCraft());
        //System.out.println(profiles);
        //List<Profile> profiles = profileService.listAll();
        model.addAttribute("profiles", profiles);
        return "profiles/search";
    }

    @GetMapping("/{id}")
    public String viewProfile(@PathVariable("id") int id, Model model){
        Profile profile = profileService.get(id);
        model.addAttribute("profile", profile);
        return "profiles/view";
    }

}
