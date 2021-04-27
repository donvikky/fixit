package com.fixit.web.controller;

import com.fixit.web.entity.State;
import com.fixit.web.service.StateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/states")
public class StateController {

    private static final Logger log = LoggerFactory.getLogger(State.class);

    @Autowired
    private StateService stateService;

    @GetMapping
    public String getStates(Model model){
        List<State> states = stateService.listAll();
        model.addAttribute("states", states);
        model.addAttribute("state", new State());
        return "states/index";
    }

    @GetMapping("/create")
    public String addState(Model model){
        State state = new State();
        model.addAttribute("state", state);
        return "states/create";
    }

    @PostMapping("/create")
    public String createState(@Valid State state, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "states/create";
        }
        log.info("The received state is "+ state);
        stateService.save(state);

        return "redirect:/states";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        State state = stateService.get(id);
        List<State> states = stateService.listAll();
        if(state == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("state", state);
        model.addAttribute("states", states);
        return "states/edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("state") @Valid State state, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "states/edit";
        }
        stateService.save(state);
        return "redirect:/states";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        stateService.delete(id);
        return "redirect:/states";
    }
}
