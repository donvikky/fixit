package com.fixit.web.controller;

import com.fixit.web.entity.Category;
import com.fixit.web.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    private String listCategories(Model model){
        List<Category> categories = categoryService.listAll();
        model.addAttribute("categories", categories);
        return "categories/index";
    }

    @GetMapping("/create")
    private String showForm(Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "categories/create";
    }

    @PostMapping("/create")
    private String processForm(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "categories/create";
        }
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    private String edit(@PathVariable("id") Integer id, Model model){
        Category category = categoryService.get(id);
        if(category == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("category", category);
        return "categories/edit";
    }

    @PostMapping("/edit")
    private String update(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "categories/edit";
        }
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    private String delete(@PathVariable("id") Integer id){
        categoryService.delete(id);
        return "redirect:/categories";
    }
}
