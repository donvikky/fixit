package com.fixit.web.controller;

import com.fixit.web.entity.Profile;
import com.fixit.web.entity.Project;
import com.fixit.web.entity.ProjectPhoto;
import com.fixit.web.entity.User;
import com.fixit.web.service.FileStorageService;
import com.fixit.web.service.ProfileService;
import com.fixit.web.service.ProjectService;
import com.fixit.web.service.StateService;
import com.fixit.web.utils.AuthUtils;
import com.fixit.web.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private ProjectService projectService;
    private StateService stateService;
    private FileStorageService storageService;
    private ProfileService profileService;

    @Autowired
    public ProjectController(ProjectService projectService, StateService stateService, FileStorageService storageService,
                             ProfileService profileService) {
        this.projectService = projectService;
        this.stateService = stateService;
        this.storageService = storageService;
        this.profileService = profileService;
    }

    @GetMapping
    public String listProjects(Model model){
        List<Project> projects = projectService.findByProfile(new AuthUtils().getCurrentUser().get().getProfile());
        model.addAttribute("projects", projects);
        return "projects/list";
    }


    @GetMapping("/create")
    @PreAuthorize("authentication.principal.user.profile != null")
    public String createProject(Model model){
        model.addAttribute("project", new Project());
        model.addAttribute("states", stateService.listAll());
        return "projects/create";
    }

    @PostMapping("/create")
    @PreAuthorize("authentication.principal.user.profile != null")
    public String save(@Valid Project project, BindingResult bindingResult,
                       @RequestParam("files") MultipartFile[] files,
                       SessionStatus sessionStatus, @AuthenticationPrincipal User user){
        if(bindingResult.hasErrors()){
            return "projects/create";
        }
        if(files.length > 0){
            List<String> fileNames = storageService.saveAll(files);
            ProjectPhoto photo = new ProjectPhoto(project);
            List<ProjectPhoto> projectPhotos = photo.addPhotos(fileNames);
            project.setProjectPhotos(projectPhotos);
        }

        Profile profile = profileService.findByUser(new AuthUtils().getCurrentUser().get());
        project.setProfile(profile);
        projectService.save(project);
        sessionStatus.setComplete();

        return "redirect:/profiles";
    }

    @GetMapping("/{id}")
    public String viewProject(@PathVariable("id") int id, Model model){
        Project project = projectService.get(id);
        long postDuration = new DateUtils().getTimeInterval(Timestamp.valueOf(project.getCreatedAt()), TimeUnit.DAYS);
        model.addAttribute("project", project);
        model.addAttribute("postDuration", postDuration);
        return "projects/view";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("authentication.principal.user.profile != null")
    public String editProject(@PathVariable("id") int id, Model model){
        Project project = projectService.get(id);
        model.addAttribute("project", project);
        model.addAttribute("states", stateService.listAll());
        return "projects/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("authentication.principal.user.profile != null")
    public String updateProject(@Valid Project project, BindingResult bindingResult,
                       @RequestParam("files") MultipartFile[] files,
                       SessionStatus sessionStatus, @AuthenticationPrincipal User user){
        if(bindingResult.hasErrors()){
            return "projects/edit";
        }
        if(files.length > 0){
            List<String> fileNames = storageService.saveAll(files);
            ProjectPhoto photo = new ProjectPhoto(project);
            List<ProjectPhoto> projectPhotos = photo.addPhotos(fileNames);
            project.setProjectPhotos(projectPhotos);
        }

        Profile profile = profileService.findByUser(new AuthUtils().getCurrentUser().get());
        project.setProfile(profile);
        projectService.save(project);
        sessionStatus.setComplete();

        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("authentication.principal.user.profile != null")
    public String delete(@PathVariable("id") int id){
        List<String> filenames = new ArrayList<>();
        projectService.get(id).getProjectPhotos().stream().forEach( file -> filenames.add(file.getPhoto()));
        projectService.delete(id);
        storageService.deleteAll(filenames);
        return "redirect:/projects";
    }

}
