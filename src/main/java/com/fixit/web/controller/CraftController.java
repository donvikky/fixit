package com.fixit.web.controller;

import com.fixit.web.entity.Category;
import com.fixit.web.entity.Craft;
import com.fixit.web.service.CategoryService;
import com.fixit.web.service.CraftService;
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
@RequestMapping("/crafts")
public class CraftController {

    private CategoryService categoryService;
    private CraftService craftService;

    @Autowired
    public CraftController(CategoryService categoryService, CraftService craftService) {
        this.categoryService = categoryService;
        this.craftService = craftService;
    }

    @ModelAttribute("categories")
    public List<Category> getCategories(){
        return categoryService.listAll();
    }

    @GetMapping("/page/{page}")
    public String index(@PathVariable("page") Optional<Integer> curPage, Model model){
        int currentPage = curPage.orElse(1);
        Page<Craft> crafts = craftService.listAll(currentPage);
        model.addAttribute("crafts", crafts);
        model.addAttribute("currentPage", currentPage);
        return "crafts/index";
    }

    @GetMapping("/create")
    public String add(Model model){
        Craft craft = new Craft();
        model.addAttribute("craft", craft);
        return "crafts/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute(name = "craft") @Valid Craft craft, BindingResult bindingResult,
                       RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "crafts/create";
        }
        craftService.save(craft);
        redirectAttributes.addFlashAttribute("message", "The service has been added successfully");
        return "redirect:/crafts/page/1";
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
    public String update(@ModelAttribute("lga") @Valid Craft craft, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "crafts/edit";
        }
        craftService.save(craft);
        redirectAttributes.addFlashAttribute("message", "The service has been updated successfully");
        return "redirect:/crafts/page/1";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id, RedirectAttributes redirectAttributes){
        craftService.delete(id);
        redirectAttributes.addFlashAttribute("message", "The service has been deleted successfully");
        return "redirect:/crafts/page/1";
    }
}
