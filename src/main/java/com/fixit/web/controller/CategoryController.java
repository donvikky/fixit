package com.fixit.web.controller;

import com.fixit.web.entity.Category;
import com.fixit.web.service.CategoryService;
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
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/page/{page}")
    private String listCategories(@PathVariable("page") Optional<Integer> curPage, Model model){
        int currentPage = curPage.orElse(1);
        Page<Category> categories = categoryService.listAll(currentPage);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", currentPage);
        return "categories/index";
    }

    @GetMapping("/create")
    private String showForm(Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "categories/create";
    }

    @PostMapping("/create")
    private String processForm(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "categories/create";
        }
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("message", "The category has been added successfully");
        return "redirect:/categories/page/1";
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
    private String update(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "categories/edit";
        }
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("message", "The category has been updated successfully");
        return "redirect:/categories/page/1";
    }

    @PostMapping("/delete")
    private String delete(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes){
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("message", "The category has been deleted successfully");
        return "redirect:/categories/page/1";
    }
}
