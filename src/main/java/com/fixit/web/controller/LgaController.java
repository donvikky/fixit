package com.fixit.web.controller;

import com.fixit.web.entity.Lga;
import com.fixit.web.entity.State;
import com.fixit.web.service.LgaService;
import com.fixit.web.service.StateService;
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
@RequestMapping("/lgas")
public class LgaController {

    private StateService stateService;
    private LgaService lgaService;

    @Autowired
    public LgaController(StateService stateService, LgaService lgaService) {
        this.stateService = stateService;
        this.lgaService = lgaService;
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
    public String index(@PathVariable("page") Optional<Integer> curPage, Model model){
        int currentPage = curPage.orElse(1);
        Page<Lga> lgas = lgaService.listAll(currentPage);
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
    public String save(@ModelAttribute(name = "lga") @Valid Lga lga, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "lgas/create";
        }
        lgaService.save(lga);
        redirectAttributes.addFlashAttribute("message", "The LGA has been added successfully");
        return "redirect:/lgas/page/1";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        Lga lga = lgaService.get(id);
        if(lga == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("lga", lga);
        return "lgas/edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("lga") @Valid Lga lga, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "lgas/edit";
        }
        lgaService.save(lga);
        redirectAttributes.addFlashAttribute("message", "The LGA has been updated successfully");
        return "redirect:/lgas/page/1";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id, RedirectAttributes redirectAttributes){
        lgaService.delete(id);
        redirectAttributes.addFlashAttribute("message", "The LGA has been deleted successfully");
        return "redirect:/lgas/page/1";
    }
}
