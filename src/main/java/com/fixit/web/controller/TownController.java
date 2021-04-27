package com.fixit.web.controller;

import com.fixit.web.entity.Lga;
import com.fixit.web.entity.State;
import com.fixit.web.entity.Town;
import com.fixit.web.service.LgaService;
import com.fixit.web.service.StateService;
import com.fixit.web.service.TownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping
    private String listTowns(Model model){
        List<Town> towns = townService.listAll();
        model.addAttribute("towns", towns);
        return "towns/index";
    }

    @GetMapping("/create")
    private String showForm(Model model){
        Town town = new Town();
        List<State> states = stateService.listAll();
        List<Lga> lgas = lgaService.listAll();
        model.addAttribute("town", town);
        model.addAttribute("states", states);
        model.addAttribute("lgas", lgas);
        return "towns/create";
    }

    @PostMapping("/create")
    private String processForm(@ModelAttribute("town") @Valid Town town, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "towns/create";
        }
        townService.save(town);
        return "redirect:/towns";
    }

    @GetMapping("/edit/{id}")
    private String edit(@PathVariable("id") Integer id, Model model){
        Town town = townService.get(id);
        if(town == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        List<State> states = stateService.listAll();
        List<Lga> lgas = lgaService.listAll();
        model.addAttribute("town", town);
        model.addAttribute("states", states);
        model.addAttribute("lgas", lgas);
        return "towns/edit";
    }

    @PostMapping("/edit")
    private String update(@ModelAttribute("town") @Valid Town town, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "towns/edit";
        }
        townService.save(town);
        return "redirect:/towns";
    }

    @GetMapping("/delete/{id}")
    private String delete(@PathVariable("id") Integer id){
        townService.delete(id);
        return "redirect:/towns";
    }
}
