package com.fixit.web.controller;

import com.fixit.web.entity.Lga;
import com.fixit.web.entity.State;
import com.fixit.web.entity.Town;
import com.fixit.web.service.LgaService;
import com.fixit.web.service.StateService;
import com.fixit.web.service.TownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/towns")
public class TownController {

    private TownService townService;
    private StateService stateService;
    private LgaService lgaService;

    @Autowired
    public TownController(TownService townService, StateService stateService, LgaService lgaService) {
        this.townService = townService;
        this.stateService = stateService;
        this.lgaService = lgaService;
    }

    public TownController(TownService townService) {
        this.townService = townService;
    }

    /*
     * Ensures the states variable is available in all templates
     * in this controller
     */
    @ModelAttribute("states")
    public List<State> getStates(){
        return stateService.listAll();
    }

    /*
     * Ensures the lgas variable is available in all templates
     * in this controller
     */
    @ModelAttribute("lgas")
    public List<Lga> getLgas(){
        return lgaService.listAll();
    }

    @GetMapping("/page/{page}")
    private String listTowns(@PathVariable("page") Optional<Integer> curPage, Model model){
        int currentPage = curPage.orElse(1);
        Page<Town> towns = townService.listAll(currentPage);
        model.addAttribute("towns", towns);
        model.addAttribute("currentPage", currentPage);
        return "towns/index";
    }

    @GetMapping("/create")
    private String showForm(Model model){
        Town town = new Town();
        model.addAttribute("town", town);
        return "towns/create";
    }

    @PostMapping("/create")
    private String processForm(@ModelAttribute("town") @Valid Town town, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "towns/create";
        }
        townService.save(town);
        redirectAttributes.addFlashAttribute("message", "The town has been added successfully");
        return "redirect:/towns/page/1";
    }

    @GetMapping("/edit/{id}")
    private String edit(@PathVariable("id") Integer id, Model model){
        Town town = townService.get(id);
        if(town == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        List<State> states = stateService.listAll();
        model.addAttribute("town", town);
        return "towns/edit";
    }

    @PostMapping("/edit")
    private String update(@ModelAttribute("town") @Valid Town town, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "towns/edit";
        }
        townService.save(town);
        redirectAttributes.addFlashAttribute("message", "The town has been updated successfully");
        return "redirect:/towns/page/1";
    }

    @PostMapping("/delete")
    private String delete(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes){
        townService.delete(id);
        redirectAttributes.addFlashAttribute("message", "The town has been deleted successfully");
        return "redirect:/towns/page/1";
    }
}
