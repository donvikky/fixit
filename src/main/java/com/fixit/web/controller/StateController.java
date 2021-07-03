package com.fixit.web.controller;

import com.fixit.web.entity.State;
import com.fixit.web.service.StateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/states")
public class StateController {

    private static final Logger log = LoggerFactory.getLogger(State.class);
    private StateService stateService;

    @Autowired
    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping("/page/{page}")
    public String getStates(@PathVariable("page") Optional<Integer> curPage, Model model){
        int currentPage = curPage.orElse(1);
        Page<State> states = stateService.listAllPaginated(currentPage);
        model.addAttribute("states", states);
        model.addAttribute("currentPage", currentPage);
        return "states/list";
    }

    @GetMapping("/create")
    public String addState(Model model){
        State state = new State();
        model.addAttribute("state", state);
        return "states/create";
    }

    @PostMapping("/create")
    public String createState(@Valid State state, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "states/create";
        }
        stateService.save(state);
        redirectAttributes.addFlashAttribute("message", "The state has been added successfully");
        return "redirect:/states/page/1";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        State state = stateService.get(id);
        List<State> states = stateService.listAll();

        model.addAttribute("state", state);
        model.addAttribute("states", states);
        return "states/edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("state") @Valid State state, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "states/edit";
        }
        stateService.save(state);
        redirectAttributes.addFlashAttribute("message", "The state has been updated successfully");
        return "redirect:/states/page/1";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id){
        stateService.delete(id);
        return "redirect:/states/page/1";
    }
}
