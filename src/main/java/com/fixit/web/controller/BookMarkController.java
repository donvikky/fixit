package com.fixit.web.controller;

import com.fixit.web.entity.BookMark;
import com.fixit.web.entity.Profile;
import com.fixit.web.service.BookMarkService;
import com.fixit.web.service.ProfileService;
import com.fixit.web.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/bookmarks")
public class BookMarkController {

    private BookMarkService bookMarkService;
    private ProfileService profileService;
    private AuthUtils authUtils;

    @Autowired
    public BookMarkController(BookMarkService bookMarkService, ProfileService profileService,
                              AuthUtils authUtils) {
        this.bookMarkService = bookMarkService;
        this.profileService = profileService;
        this.authUtils = authUtils;
    }

    @GetMapping("/page/{page}")
    public String getStates(@PathVariable("page") Optional<Integer> curPage, Model model){
        int currentPage = curPage.orElse(1);
        Profile bookmarker = authUtils.getCurrentUser().get().getProfile();
        Page<BookMark> bookmarks = bookMarkService.findByBookmarker(bookmarker, currentPage);
        model.addAttribute("bookmarks", bookmarks);
        model.addAttribute("currentPage", currentPage);
        return "bookmarks/list";
    }

    @PostMapping("/add")
    public String saveBookMark(@RequestParam("id") int id, RedirectAttributes redirectAttributes){
        Profile artisan = profileService.get(id);
        Profile bookmarker = authUtils.getCurrentUser().get().getProfile();
        Optional<BookMark> bookMarkOptional = bookMarkService.findByBookmarkerAndArtisan(bookmarker, artisan);

        if(bookMarkOptional.isPresent()){
            redirectAttributes.addFlashAttribute("message",
                    "You have already bookmarked this profile");
            return "redirect:/profiles/view/"+ id;
        }

        BookMark bookMark = new BookMark(bookmarker, artisan);
        bookMarkService.save(bookMark);
        redirectAttributes.addFlashAttribute("message",
                "This profile has been added to your bookmarks");
        return "redirect:/profiles/view/"+ id;
    }

    @PostMapping("/delete")
    public String deleteBookMark(@RequestParam("id") int id, RedirectAttributes redirectAttributes){
        bookMarkService.delete(id);
        redirectAttributes.addFlashAttribute("message", "The bookmark has been deleted successfully");
        return "redirect:/bookmarks/page/1";
    }
}
