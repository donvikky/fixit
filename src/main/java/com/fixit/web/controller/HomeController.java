package com.fixit.web.controller;

import com.fixit.web.entity.Craft;
import com.fixit.web.model.ProfileSearch;
import com.fixit.web.service.CraftService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private CraftService craftService;

    public HomeController(CraftService craftService) {
        this.craftService = craftService;
    }

    @GetMapping
    public String home(Model model){
        ProfileSearch search = new ProfileSearch();
        List<Craft> crafts = craftService.listAll();
        model.addAttribute("search", search);
        model.addAttribute("crafts", crafts);

        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        return "dashboard";
    }
}
