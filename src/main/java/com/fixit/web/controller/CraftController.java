package com.fixit.web.controller;

import com.fixit.web.entity.Category;
import com.fixit.web.entity.Craft;
import com.fixit.web.service.CategoryService;
import com.fixit.web.service.CraftService;
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
@RequestMapping("/crafts")
public class CraftController {

    private CategoryService categoryService;
    private CraftService craftService;

    @Autowired
    public CraftController(CategoryService categoryService, CraftService craftService) {
        this.categoryService = categoryService;
        this.craftService = craftService;
    }

    @GetMapping
    public String index(Model model){
        List<Craft> crafts = craftService.listAll();
        model.addAttribute("crafts", crafts);
        return "crafts/index";
    }

    @GetMapping("/create")
    public String add(Model model){
        Craft craft = new Craft();
        List<Category> categories = categoryService.listAll();

        model.addAttribute("craft", craft);
        model.addAttribute("categories", categories);
        return "crafts/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute(name = "craft") @Valid Craft craft, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "crafts/create";
        }
        craftService.save(craft);
        return "redirect:/crafts";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        Craft craft = craftService.get(id);
        List<Category> categories = categoryService.listAll();
        if(craft == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("craft", craft);
        model.addAttribute("categories", categories);
        return "crafts/edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("lga") @Valid Craft craft, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "crafts/edit";
        }
        craftService.save(craft);
        return "redirect:/crafts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        craftService.delete(id);
        return "redirect:/crafts";
    }
}
