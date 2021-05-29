package com.fixit.web.controller;

import com.fixit.web.entity.Profile;
import com.fixit.web.entity.Project;
import com.fixit.web.entity.ProjectPhoto;
import com.fixit.web.entity.State;
import com.fixit.web.service.FileStorageService;
import com.fixit.web.service.ProfileService;
import com.fixit.web.service.ProjectService;
import com.fixit.web.service.StateService;
import com.fixit.web.utils.AuthUtils;
import com.fixit.web.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private ProjectService projectService;
    private StateService stateService;
    private FileStorageService storageService;
    private ProfileService profileService;
    private AuthUtils authUtils;

    @Autowired
    public ProjectController(ProjectService projectService, StateService stateService, FileStorageService storageService,
                             ProfileService profileService, AuthUtils authUtils) {
        this.projectService = projectService;
        this.stateService = stateService;
        this.storageService = storageService;
        this.profileService = profileService;
        this.authUtils = authUtils;
    }

    /*
     * Ensures the states variable is available in all templates
     * in this controller
     */
    @ModelAttribute("states")
    public List<State> getStates(){
        return stateService.listAll();
    }

    @GetMapping("/page/{page}")
    public String listProjects(Model model, @PathVariable("page")Optional<Integer> curPage){
        int currentPage = curPage.orElse(1);
        Page page = projectService.findByProfile(authUtils.getCurrentUser().get().getProfile(), currentPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("page", page);
        return "projects/list";
    }

    @GetMapping("/create")
    public String createProject(Model model){
        model.addAttribute("project", new Project());
        return "projects/create";
    }

    @PostMapping("/create")
    public String save(@Valid Project project, BindingResult bindingResult,
                       @RequestParam("files") MultipartFile[] files,
                       SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){
            return "projects/create";
        }
        if(files.length > 0){
            List<String> fileNames = storageService.saveAll(files);
            ProjectPhoto photo = new ProjectPhoto(project);
            List<ProjectPhoto> projectPhotos = photo.addPhotos(fileNames);
            project.setProjectPhotos(projectPhotos);
        }

        Profile profile = profileService.findByUser(authUtils.getCurrentUser().get());
        project.setProfile(profile);
        projectService.save(project);
        sessionStatus.setComplete();

        return "redirect:/projects";
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
    public String editProject(@PathVariable("id") int id, Model model){
        Project project = projectService.get(id);
        model.addAttribute("project", project);
        return "projects/edit";
    }

    @PostMapping("/edit")
    public String updateProject(@Valid Project project, BindingResult bindingResult,
                       @RequestParam("files") MultipartFile[] files,
                       SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){
            return "projects/edit";
        }
        if(files.length > 0){
            List<String> fileNames = storageService.saveAll(files);
            ProjectPhoto photo = new ProjectPhoto(project);
            List<ProjectPhoto> projectPhotos = photo.addPhotos(fileNames);
            project.setProjectPhotos(projectPhotos);
        }

        Profile profile = profileService.findByUser(authUtils.getCurrentUser().get());
        project.setProfile(profile);
        projectService.save(project);
        sessionStatus.setComplete();

        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        List<String> filenames = new ArrayList<>();
        projectService.get(id).getProjectPhotos().stream().forEach( file -> filenames.add(file.getPhoto()));
        projectService.delete(id);
        storageService.deleteAll(filenames);
        return "redirect:/projects";
    }


}
