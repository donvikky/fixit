package com.fixit.web.controller;

import com.fixit.web.entity.Lga;
import com.fixit.web.entity.State;
import com.fixit.web.service.LgaService;
import com.fixit.web.service.StateService;
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
@RequestMapping("/lgas")
public class LgaController {

    private StateService stateService;
    private LgaService lgaService;

    @Autowired
    public LgaController(StateService stateService, LgaService lgaService) {
        this.stateService = stateService;
        this.lgaService = lgaService;
    }

    @GetMapping
    public String index(Model model){
        List<Lga> lgas = lgaService.listAll();
        model.addAttribute("lgas", lgas);
        return "lgas/list";
    }

    @GetMapping("/create")
    public String add(Model model){
        Lga lga = new Lga();
        List<State> states = stateService.listAll();

        model.addAttribute("lga", new Lga());
        model.addAttribute("states", states);
        return "lgas/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute(name = "lga") @Valid Lga lga, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "lgas/create";
        }
        lgaService.save(lga);
        return "redirect:/lgas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        Lga lga = lgaService.get(id);
        List<State> states = stateService.listAll();
        if(lga == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("lga", lga);
        model.addAttribute("states", states);
        return "lgas/edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("lga") @Valid Lga lga, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "lgas/edit";
        }
        lgaService.save(lga);
        return "redirect:/lgas";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        lgaService.delete(id);
        return "redirect:/lgas";
    }
}
